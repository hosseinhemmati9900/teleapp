package com.example.data.model

enum class TelegramContentType(val titleFa: String, val titleEn: String, val iconName: String) {
    ALL("همه موارد", "All", "all"),
    VIDEO("ویدیوها و کلیپ‌ها", "Videos & Clips", "video"),
    CHANNEL("کانال‌ها", "Channels", "channel"),
    GROUP("گروه‌ها و سوپرگروه‌ها", "Groups & Communities", "group"),
    DOCUMENT("فایل و اسناد", "Files & Docs", "file"),
    AUDIO("موزیک و پادکست", "Music & Podcasts", "audio"),
    PHOTO("عکس و والپیپر", "Photos & Wallpapers", "photo"),
    BOT("ربات‌های کاربردی", "Bots", "bot")
}

enum class TelegramCategory(val titleFa: String, val titleEn: String, val emoji: String) {
    ALL("همه دسته‌ها", "All Categories", "✨"),
    GAMING("بازی و گیمینگ", "Gaming & Esports", "🎮"),
    TECH_AI("فناوری و هوش مصنوعی", "Tech & AI", "🤖"),
    MOVIES_SERIES("فیلم و سریال", "Movies & Series", "🎬"),
    PROGRAMMING("برنامه‌نویسی و کد", "Programming & Dev", "💻"),
    CRYPTO_FINANCE("ارز دیجیتال و مالی", "Crypto & Finance", "📈"),
    BOOKS_EDUCATION("کتاب و آموزش", "Books & Education", "📚"),
    MUSIC("موسیقی و آهنگ", "Music & Tracks", "🎵"),
    NEWS("اخبار و رسانه", "News & Media", "📰"),
    GRAPHICS_WALLPAPERS("گرافیک و عکس", "Wallpapers & Art", "🎨"),
    ENTERTAINMENT("سرگرمی و فان", "Entertainment", "🎉")
}

enum class SearchSortBy(val titleFa: String) {
    RELEVANCE("مرتبط‌ترین"),
    MEMBERS("بیشترین اعضا"),
    NEWEST("جدیدترین")
}

data class TelegramChannelItem(
    val id: String,
    val name: String,
    val username: String,
    val description: String,
    val memberCount: Long,
    val memberCountFormatted: String,
    val category: TelegramCategory,
    val contentType: TelegramContentType,
    val tags: List<String>,
    val avatarUrl: String? = null,
    val isVerified: Boolean = false,
    val hasVideos: Boolean = false,
    val hasFiles: Boolean = false,
    val sampleMedia: List<String> = emptyList(),
    val directTmeLink: String = "https://t.me/$username",
    val tgAppDeepLink: String = "tg://resolve?domain=$username"
)

enum class BotMediaType(val titleFa: String, val iconName: String) {
    MESSAGE("پیام متنی", "chat"),
    PHOTO("عکس / تصویر", "image"),
    VIDEO("ویدیو / کلیپ", "videocam"),
    DOCUMENT("فایل / اسناد", "description"),
    AUDIO("صدا / موزیک", "audiotrack")
}

data class BotUploadStatus(
    val isUploading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val progressMessage: String? = null
)
