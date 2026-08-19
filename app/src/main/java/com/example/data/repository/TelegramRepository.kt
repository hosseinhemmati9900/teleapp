package com.example.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.data.local.AppDatabase
import com.example.data.local.BookmarkEntity
import com.example.data.local.BotSentHistoryEntity
import com.example.data.local.SearchHistoryEntity
import com.example.data.model.BotMediaType
import com.example.data.model.BotUploadStatus
import com.example.data.model.SearchSortBy
import com.example.data.model.TelegramCategory
import com.example.data.model.TelegramChannelItem
import com.example.data.model.TelegramContentType
import com.example.data.remote.TelegramBotClientProvider
import com.example.data.remote.TelegramBotConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.text.DecimalFormat

class TelegramRepository(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val dao = db.telegramDao()
    private val botService = TelegramBotClientProvider.service

    val allBookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    val searchHistory: Flow<List<SearchHistoryEntity>> = dao.getSearchHistory()
    val botSentHistory: Flow<List<BotSentHistoryEntity>> = dao.getBotSentHistory()

    fun isBookmarked(username: String): Flow<Boolean> = dao.isBookmarked(username)

    suspend fun toggleBookmark(item: TelegramChannelItem, isCurrentlyBookmarked: Boolean) {
        if (isCurrentlyBookmarked) {
            dao.deleteBookmark(item.username)
        } else {
            dao.insertBookmark(
                BookmarkEntity(
                    username = item.username,
                    name = item.name,
                    description = item.description,
                    category = item.category.name,
                    contentType = item.contentType.name,
                    memberCountFormatted = item.memberCountFormatted,
                    directTmeLink = item.directTmeLink
                )
            )
        }
    }

    suspend fun recordSearch(query: String, category: TelegramCategory, contentType: TelegramContentType) {
        if (query.isNotBlank()) {
            dao.insertSearchHistory(
                SearchHistoryEntity(
                    query = query.trim(),
                    category = category.name,
                    contentType = contentType.name
                )
            )
        }
    }

    suspend fun deleteSearchHistoryItem(id: Long) = dao.deleteSearchHistoryItem(id)
    suspend fun clearSearchHistory() = dao.clearSearchHistory()
    suspend fun deleteBotHistoryItem(id: Long) = dao.deleteBotSentHistoryItem(id)
    suspend fun clearBotHistory() = dao.clearBotSentHistory()

    fun searchItems(
        query: String,
        selectedCategory: TelegramCategory,
        selectedContentType: TelegramContentType,
        onlyVerified: Boolean = false,
        onlyWithVideos: Boolean = false,
        onlyWithFiles: Boolean = false,
        sortBy: SearchSortBy = SearchSortBy.RELEVANCE
    ): List<TelegramChannelItem> {
        val cleanQuery = query.trim().lowercase()

        var list = TelegramSearchDataset.items.filter { item ->
            // Category filter
            val matchesCategory = (selectedCategory == TelegramCategory.ALL || item.category == selectedCategory)

            // Content Type filter
            val matchesContentType = (selectedContentType == TelegramContentType.ALL || item.contentType == selectedContentType)

            // Verified filter
            val matchesVerified = !onlyVerified || item.isVerified

            // Video filter
            val matchesVideos = !onlyWithVideos || item.hasVideos

            // Files filter
            val matchesFiles = !onlyWithFiles || item.hasFiles

            // Query match
            val matchesQuery = if (cleanQuery.isEmpty()) {
                true
            } else {
                item.name.lowercase().contains(cleanQuery) ||
                item.username.lowercase().contains(cleanQuery) ||
                item.description.lowercase().contains(cleanQuery) ||
                item.tags.any { it.lowercase().contains(cleanQuery) } ||
                item.sampleMedia.any { it.lowercase().contains(cleanQuery) }
            }

            matchesCategory && matchesContentType && matchesVerified && matchesVideos && matchesFiles && matchesQuery
        }

        // Sorting
        list = when (sortBy) {
            SearchSortBy.MEMBERS -> list.sortedByDescending { it.memberCount }
            SearchSortBy.NEWEST -> list.reversed()
            SearchSortBy.RELEVANCE -> {
                if (cleanQuery.isEmpty()) {
                    list.sortedByDescending { it.memberCount }
                } else {
                    list.sortedByDescending { item ->
                        var score = 0
                        if (item.name.lowercase().contains(cleanQuery)) score += 10
                        if (item.username.lowercase().contains(cleanQuery)) score += 8
                        if (item.tags.any { it.lowercase() == cleanQuery }) score += 15
                        if (item.tags.any { it.lowercase().contains(cleanQuery) }) score += 5
                        if (item.description.lowercase().contains(cleanQuery)) score += 3
                        score
                    }
                }
            }
        }

        return list
    }

    // Helper: Generate Deep Links
    fun generateTelegramHashtagUrl(tag: String): String {
        val clean = tag.replace("#", "").trim()
        return "tg://search_hashtag?hashtag=$clean"
    }

    fun generateTelegramSearchQueryUrl(query: String): String {
        val encoded = URLEncoder.encode(query, "UTF-8")
        return "tg://resolve?domain=telegram&q=$encoded"
    }

    fun generateGoogleTelegramDorkUrl(query: String, type: TelegramContentType): String {
        val dorkQuery = when (type) {
            TelegramContentType.VIDEO -> "site:t.me filetype:mp4 OR filetype:mkv \"$query\""
            TelegramContentType.DOCUMENT -> "site:t.me filetype:pdf OR filetype:apk OR filetype:zip \"$query\""
            TelegramContentType.AUDIO -> "site:t.me filetype:mp3 OR filetype:flac \"$query\""
            TelegramContentType.CHANNEL -> "site:t.me/s/ \"$query\" کانال"
            TelegramContentType.GROUP -> "site:t.me \"joinchat\" OR \"+*\" \"$query\""
            else -> "site:t.me \"$query\""
        }
        val encoded = URLEncoder.encode(dorkQuery, "UTF-8")
        return "https://www.google.com/search?q=$encoded"
    }

    // Helper: Send to Telegram Bot
    suspend fun sendToBot(
        mediaType: BotMediaType,
        textMessage: String,
        fileUri: Uri?,
        caption: String?,
        senderAlias: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val chatId = TelegramBotConstants.DEFAULT_CHAT_ID
            val formattedSender = if (!senderAlias.isNullOrBlank()) "👤 فرستنده: $senderAlias\n" else ""

            when (mediaType) {
                BotMediaType.MESSAGE -> {
                    val fullText = "$formattedSender📝 <b>پیام دریافتی از اپلیکیشن TeleFinder:</b>\n\n${textMessage.trim()}"
                    val response = botService.sendMessage(
                        chatId = chatId,
                        text = fullText,
                        parseMode = "HTML"
                    )

                    if (response.isSuccessful && response.body()?.ok == true) {
                        dao.insertBotSentHistory(
                            BotSentHistoryEntity(
                                type = "MESSAGE",
                                contentSummary = textMessage.take(80),
                                status = "SUCCESS"
                            )
                        )
                        Result.success("پیام متنی شما با موفقیت به ربات ارسال شد!")
                    } else {
                        val err = response.body()?.description ?: response.errorBody()?.string() ?: "خطا در ارسال پیام به تلگرام"
                        Result.failure(Exception(err))
                    }
                }

                BotMediaType.PHOTO, BotMediaType.VIDEO, BotMediaType.DOCUMENT, BotMediaType.AUDIO -> {
                    if (fileUri == null) {
                        return@withContext Result.failure(Exception("هیچ فایلی انتخاب نشده است."))
                    }

                    val tempFile = createTempFileFromUri(fileUri)
                        ?: return@withContext Result.failure(Exception("خطا در خواندن فایل انتخاب شده."))

                    val fileName = getFileName(fileUri) ?: tempFile.name
                    val fileSizeFormatted = formatFileSize(tempFile.length())
                    val mimeType = context.contentResolver.getType(fileUri) ?: "application/octet-stream"

                    val requestFile = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
                    val partName = when (mediaType) {
                        BotMediaType.PHOTO -> "photo"
                        BotMediaType.VIDEO -> "video"
                        BotMediaType.DOCUMENT -> "document"
                        BotMediaType.AUDIO -> "audio"
                        else -> "document"
                    }
                    val bodyPart = MultipartBody.Part.createFormData(partName, fileName, requestFile)
                    val chatIdBody = chatId.toRequestBody("text/plain".toMediaTypeOrNull())
                    val fullCaption = buildString {
                        if (formattedSender.isNotEmpty()) append(formattedSender)
                        append("📁 <b>ارسال شده از TeleFinder:</b> ${mediaType.titleFa}\n")
                        append("📄 نام فایل: <code>$fileName</code> ($fileSizeFormatted)\n")
                        if (!caption.isNullOrBlank()) {
                            append("\n💬 توضیحات: ${caption.trim()}")
                        }
                    }.toRequestBody("text/plain".toMediaTypeOrNull())
                    val parseModeBody = "HTML".toRequestBody("text/plain".toMediaTypeOrNull())

                    val response = when (mediaType) {
                        BotMediaType.PHOTO -> botService.sendPhoto(chatIdBody, bodyPart, fullCaption, parseModeBody)
                        BotMediaType.VIDEO -> botService.sendVideo(chatIdBody, bodyPart, fullCaption, parseModeBody)
                        BotMediaType.DOCUMENT -> botService.sendDocument(chatIdBody, bodyPart, fullCaption, parseModeBody)
                        BotMediaType.AUDIO -> botService.sendAudio(chatIdBody, bodyPart, fullCaption, parseModeBody)
                        else -> botService.sendDocument(chatIdBody, bodyPart, fullCaption, parseModeBody)
                    }

                    // Clean up temp file
                    tempFile.delete()

                    if (response.isSuccessful && response.body()?.ok == true) {
                        dao.insertBotSentHistory(
                            BotSentHistoryEntity(
                                type = mediaType.name,
                                contentSummary = caption?.take(60) ?: fileName,
                                fileName = fileName,
                                fileSizeFormatted = fileSizeFormatted,
                                status = "SUCCESS"
                            )
                        )
                        Result.success("${mediaType.titleFa} با موفقیت به ربات ارسال گردید!")
                    } else {
                        val err = response.body()?.description ?: response.errorBody()?.string() ?: "خطا در ارسال فایل به ربات تلگرام"
                        Result.failure(Exception(err))
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun pingBot(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = botService.getMe()
            if (response.isSuccessful && response.body()?.ok == true) {
                val user = response.body()?.result
                Result.success("✅ ربات فعال است: @${user?.username ?: user?.firstName} (ID: ${user?.id})")
            } else {
                Result.failure(Exception("خطا در پاسخگویی ربات: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun createTempFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = getFileName(uri) ?: "upload_${System.currentTimeMillis()}"
            val tempFile = File(context.cacheDir, fileName)
            val outputStream = FileOutputStream(tempFile)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            null
        }
    }

    fun getFileName(uri: Uri): String? {
        var name: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) {
                        name = it.getString(index)
                    }
                }
            }
        }
        if (name == null) {
            name = uri.path?.substringAfterLast('/')
        }
        return name
    }

    fun getFileSizeFormatted(uri: Uri): String {
        var sizeBytes: Long = 0
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val index = it.getColumnIndex(OpenableColumns.SIZE)
                    if (index >= 0) {
                        sizeBytes = it.getLong(index)
                    }
                }
            }
        }
        return formatFileSize(sizeBytes)
    }

    private fun formatFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        val formatted = DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble()))
        return "$formatted ${units[digitGroups.coerceAtMost(units.size - 1)]}"
    }
}
