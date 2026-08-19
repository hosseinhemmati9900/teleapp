package com.example.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BookmarkEntity
import com.example.data.local.BotSentHistoryEntity
import com.example.data.local.SearchHistoryEntity
import com.example.data.model.BotMediaType
import com.example.data.model.BotUploadStatus
import com.example.data.model.SearchSortBy
import com.example.data.model.TelegramCategory
import com.example.data.model.TelegramChannelItem
import com.example.data.model.TelegramContentType
import com.example.data.repository.TelegramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

data class SearchUiState(
    val query: String = "",
    val selectedCategory: TelegramCategory = TelegramCategory.ALL,
    val selectedContentType: TelegramContentType = TelegramContentType.ALL,
    val onlyVerified: Boolean = false,
    val onlyWithVideos: Boolean = false,
    val onlyWithFiles: Boolean = false,
    val sortBy: SearchSortBy = SearchSortBy.RELEVANCE,
    val results: List<TelegramChannelItem> = emptyList(),
    val totalCount: Int = 0
)

data class BotSenderUiState(
    val selectedType: BotMediaType = BotMediaType.MESSAGE,
    val textMessage: String = "",
    val caption: String = "",
    val senderAlias: String = "",
    val selectedFileUri: Uri? = null,
    val selectedFileName: String? = null,
    val selectedFileSize: String? = null,
    val uploadStatus: BotUploadStatus = BotUploadStatus(),
    val botPingResult: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TelegramRepository(application)

    // Navigation State
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    fun setTab(index: Int) {
        _currentTab.value = index
    }

    // Search States
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow(TelegramCategory.ALL)
    private val _selectedContentType = MutableStateFlow(TelegramContentType.ALL)
    private val _onlyVerified = MutableStateFlow(false)
    private val _onlyWithVideos = MutableStateFlow(false)
    private val _onlyWithFiles = MutableStateFlow(false)
    private val _sortBy = MutableStateFlow(SearchSortBy.RELEVANCE)

    val searchUiState: StateFlow<SearchUiState> = combine(
        combine(_searchQuery, _selectedCategory, _selectedContentType) { q, cat, type ->
            Triple(q, cat, type)
        },
        combine(_onlyVerified, _onlyWithVideos, _onlyWithFiles, _sortBy) { ver, vid, fil, sort ->
            Quad(ver, vid, fil, sort)
        }
    ) { (q, cat, type), (ver, vid, fil, sort) ->
        val results = repository.searchItems(
            query = q,
            selectedCategory = cat,
            selectedContentType = type,
            onlyVerified = ver,
            onlyWithVideos = vid,
            onlyWithFiles = fil,
            sortBy = sort
        )
        SearchUiState(
            query = q,
            selectedCategory = cat,
            selectedContentType = type,
            onlyVerified = ver,
            onlyWithVideos = vid,
            onlyWithFiles = fil,
            sortBy = sort,
            results = results,
            totalCount = results.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState(
            results = repository.searchItems("", TelegramCategory.ALL, TelegramContentType.ALL),
            totalCount = repository.searchItems("", TelegramCategory.ALL, TelegramContentType.ALL).size
        )
    )

    // Bot Sender State
    private val _botSenderUiState = MutableStateFlow(BotSenderUiState())
    val botSenderUiState: StateFlow<BotSenderUiState> = _botSenderUiState.asStateFlow()

    // Database Flow Observations
    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchHistory: StateFlow<List<SearchHistoryEntity>> = repository.searchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val botSentHistory: StateFlow<List<BotSentHistoryEntity>> = repository.botSentHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search Actions
    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelect(category: TelegramCategory) {
        _selectedCategory.value = category
    }

    fun onContentTypeSelect(contentType: TelegramContentType) {
        _selectedContentType.value = contentType
    }

    fun toggleVerifiedOnly() {
        _onlyVerified.value = !_onlyVerified.value
    }

    fun toggleVideosOnly() {
        _onlyWithVideos.value = !_onlyWithVideos.value
    }

    fun toggleFilesOnly() {
        _onlyWithFiles.value = !_onlyWithFiles.value
    }

    fun setSortBy(sortBy: SearchSortBy) {
        _sortBy.value = sortBy
    }

    fun executeSearchAndSaveHistory() {
        val query = _searchQuery.value
        if (query.isNotBlank()) {
            viewModelScope.launch {
                repository.recordSearch(query, _selectedCategory.value, _selectedContentType.value)
            }
        }
    }

    fun applyHistorySearch(item: SearchHistoryEntity) {
        _searchQuery.value = item.query
        val category = TelegramCategory.entries.find { it.name == item.category } ?: TelegramCategory.ALL
        val contentType = TelegramContentType.entries.find { it.name == item.contentType } ?: TelegramContentType.ALL
        _selectedCategory.value = category
        _selectedContentType.value = contentType
        _currentTab.value = 0
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch { repository.deleteSearchHistoryItem(id) }
    }

    fun clearAllHistory() {
        viewModelScope.launch { repository.clearSearchHistory() }
    }

    // Bookmark Actions
    fun toggleBookmark(item: TelegramChannelItem, isBookmarked: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(item, isBookmarked)
        }
    }

    // Bot Sender Actions
    fun setBotMediaType(type: BotMediaType) {
        _botSenderUiState.value = _botSenderUiState.value.copy(
            selectedType = type,
            uploadStatus = BotUploadStatus()
        )
    }

    fun setBotTextMessage(text: String) {
        _botSenderUiState.value = _botSenderUiState.value.copy(textMessage = text)
    }

    fun setBotCaption(caption: String) {
        _botSenderUiState.value = _botSenderUiState.value.copy(caption = caption)
    }

    fun setSenderAlias(alias: String) {
        _botSenderUiState.value = _botSenderUiState.value.copy(senderAlias = alias)
    }

    fun onFileSelected(uri: Uri?) {
        if (uri != null) {
            val fileName = repository.getFileName(uri)
            val fileSize = repository.getFileSizeFormatted(uri)
            _botSenderUiState.value = _botSenderUiState.value.copy(
                selectedFileUri = uri,
                selectedFileName = fileName,
                selectedFileSize = fileSize,
                uploadStatus = BotUploadStatus()
            )
        } else {
            _botSenderUiState.value = _botSenderUiState.value.copy(
                selectedFileUri = null,
                selectedFileName = null,
                selectedFileSize = null
            )
        }
    }

    fun clearSelectedFile() {
        _botSenderUiState.value = _botSenderUiState.value.copy(
            selectedFileUri = null,
            selectedFileName = null,
            selectedFileSize = null
        )
    }

    fun applyQuickTemplate(templateText: String) {
        if (_botSenderUiState.value.selectedType == BotMediaType.MESSAGE) {
            _botSenderUiState.value = _botSenderUiState.value.copy(textMessage = templateText)
        } else {
            _botSenderUiState.value = _botSenderUiState.value.copy(caption = templateText)
        }
    }

    fun sendToBot() {
        val state = _botSenderUiState.value
        if (state.selectedType == BotMediaType.MESSAGE && state.textMessage.isBlank()) {
            _botSenderUiState.value = state.copy(
                uploadStatus = BotUploadStatus(errorMessage = "لطفاً متن پیام خود را وارد کنید.")
            )
            return
        }

        if (state.selectedType != BotMediaType.MESSAGE && state.selectedFileUri == null) {
            _botSenderUiState.value = state.copy(
                uploadStatus = BotUploadStatus(errorMessage = "لطفاً یک فایل برای ارسال انتخاب کنید.")
            )
            return
        }

        _botSenderUiState.value = state.copy(
            uploadStatus = BotUploadStatus(isUploading = true, progressMessage = "در حال ارسال به ربات تلگرام...")
        )

        viewModelScope.launch {
            val result = repository.sendToBot(
                mediaType = state.selectedType,
                textMessage = state.textMessage,
                fileUri = state.selectedFileUri,
                caption = state.caption,
                senderAlias = state.senderAlias
            )

            if (result.isSuccess) {
                _botSenderUiState.value = _botSenderUiState.value.copy(
                    uploadStatus = BotUploadStatus(isSuccess = true, progressMessage = result.getOrNull()),
                    textMessage = "",
                    caption = "",
                    selectedFileUri = null,
                    selectedFileName = null,
                    selectedFileSize = null
                )
            } else {
                _botSenderUiState.value = _botSenderUiState.value.copy(
                    uploadStatus = BotUploadStatus(
                        isSuccess = false,
                        errorMessage = result.exceptionOrNull()?.message ?: "خطای ناشناخته در ارسال"
                    )
                )
            }
        }
    }

    fun pingBot() {
        viewModelScope.launch {
            _botSenderUiState.value = _botSenderUiState.value.copy(
                botPingResult = "در حال بررسی وضعیت اتصال به ربات..."
            )
            val res = repository.pingBot()
            _botSenderUiState.value = _botSenderUiState.value.copy(
                botPingResult = res.getOrElse { "❌ خطا در اتصال: ${it.message}" }
            )
        }
    }

    fun deleteBotHistoryItem(id: Long) {
        viewModelScope.launch { repository.deleteBotHistoryItem(id) }
    }

    fun clearBotHistory() {
        viewModelScope.launch { repository.clearBotHistory() }
    }

    // Helper Link Generators
    fun getTelegramHashtagUrl(tag: String): String = repository.generateTelegramHashtagUrl(tag)
    fun getGoogleDorkUrl(query: String, type: TelegramContentType): String = repository.generateGoogleTelegramDorkUrl(query, type)
    fun getTelegramSearchQueryUrl(query: String): String = repository.generateTelegramSearchQueryUrl(query)
}
