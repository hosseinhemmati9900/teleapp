package com.example.data.repository

import com.example.data.model.TelegramCategory
import com.example.data.model.TelegramChannelItem
import com.example.data.model.TelegramContentType

object TelegramSearchDataset {
    val items: List<TelegramChannelItem> = listOf(
        // Gaming Channels & Videos
        TelegramChannelItem(
            id = "game_1",
            name = "Gaming Universe & Clips",
            username = "gaming_clips_hd",
            description = "بهترین کلیپ‌ها، تریلرها و ویدیوهای جذاب دنیای گیم و بازی‌های ویدیویی (PS5, PC, Xbox)",
            memberCount = 385000,
            memberCountFormatted = "385K",
            category = TelegramCategory.GAMING,
            contentType = TelegramContentType.VIDEO,
            tags = listOf("game", "gameplay", "gaming", "video", "ps5", "xbox", "trailer", "game clips"),
            isVerified = true,
            hasVideos = true,
            hasFiles = false,
            sampleMedia = listOf("GTA 6 New Gameplay Leak", "Elden Ring DLC Highlights", "Call of Duty Warzone Clips")
        ),
        TelegramChannelItem(
            id = "game_2",
            name = "Game Archive & APK Mods",
            username = "game_archive_mods",
            description = "دانلود مستقیم دیتا و فایل‌های نصبی بازی‌های اندروید، مود و کرک شده با لینک پرسرعت",
            memberCount = 520000,
            memberCountFormatted = "520K",
            category = TelegramCategory.GAMING,
            contentType = TelegramContentType.DOCUMENT,
            tags = listOf("game", "apk", "mod", "data", "android games", "minecraft", "fifa", "gta"),
            isVerified = true,
            hasVideos = false,
            hasFiles = true,
            sampleMedia = listOf("Minecraft PE v1.21 Mod.apk", "GTA San Andreas Full Data.zip", "PPSSPP Game ISOs")
        ),
        TelegramChannelItem(
            id = "game_3",
            name = "GameZone Community Hub",
            username = "gamezone_community",
            description = "بزرگترین سوپرگروه چت و گفتگو پیرامون بازی‌های آنلاین، مسابقات و تشکیل تیم",
            memberCount = 142000,
            memberCountFormatted = "142K",
            category = TelegramCategory.GAMING,
            contentType = TelegramContentType.GROUP,
            tags = listOf("game", "community", "group", "esports", "multiplayer", "pubg", "cod"),
            isVerified = false,
            hasVideos = true,
            hasFiles = false
        ),
        TelegramChannelItem(
            id = "game_4",
            name = "GameFinder & Deals Bot",
            username = "game_finder_deals_bot",
            description = "ربات هوشمند جستجوی تخفیف‌های استیم، پلی‌استیشن و معرفی بازی‌های رایگان هفته",
            memberCount = 89000,
            memberCountFormatted = "89K",
            category = TelegramCategory.GAMING,
            contentType = TelegramContentType.BOT,
            tags = listOf("game", "bot", "deals", "steam", "free games", "finder"),
            isVerified = true,
            hasVideos = false,
            hasFiles = false
        ),
        TelegramChannelItem(
            id = "game_5",
            name = "Retro Gaming & OST Music",
            username = "retro_game_ost",
            description = "آرشیو کامل موسیقی متن (OST) بازی‌های نوستالژیک و مدرن با کیفیت ۳۲۰ و FLAC",
            memberCount = 74000,
            memberCountFormatted = "74K",
            category = TelegramCategory.GAMING,
            contentType = TelegramContentType.AUDIO,
            tags = listOf("game", "music", "ost", "soundtrack", "retro", "audio"),
            isVerified = false,
            hasVideos = false,
            hasFiles = true
        ),
        TelegramChannelItem(
            id = "game_6",
            name = "4K Gaming Wallpapers",
            username = "gaming_wallpapers_4k",
            description = "والپیپرهای با کیفیت خارق‌العاده 4K از کاراکترها و صحنه‌های بازی‌های محبوب",
            memberCount = 110000,
            memberCountFormatted = "110K",
            category = TelegramCategory.GAMING,
            contentType = TelegramContentType.PHOTO,
            tags = listOf("game", "wallpaper", "art", "photo", "4k", "backgrounds"),
            isVerified = false,
            hasVideos = false,
            hasFiles = true
        ),

        // Tech & AI
        TelegramChannelItem(
            id = "tech_1",
            name = "AI Tools & Daily Prompts",
            username = "ai_tools_daily",
            description = "جدیدترین اخبار هوش مصنوعی، مدل‌های زبانی، ابزارهای کاربردی و پرامپت‌های تولید تصویر",
            memberCount = 430000,
            memberCountFormatted = "430K",
            category = TelegramCategory.TECH_AI,
            contentType = TelegramContentType.CHANNEL,
            tags = listOf("ai", "tech", "chatgpt", "gemini", "tools", "artificial intelligence"),
            isVerified = true,
            hasVideos = true,
            hasFiles = true
        ),
        TelegramChannelItem(
            id = "tech_2",
            name = "Tech Video Reviews & Teardowns",
            username = "tech_video_reviews",
            description = "ویدیوهای جعبه‌گشایی، بررسی تخصصی گوشی‌های پرچمدار، لپ‌تاپ و گجت‌های هوشمند",
            memberCount = 295000,
            memberCountFormatted = "295K",
            category = TelegramCategory.TECH_AI,
            contentType = TelegramContentType.VIDEO,
            tags = listOf("tech", "video", "review", "smartphone", "gadgets", "hardware"),
            isVerified = true,
            hasVideos = true,
            hasFiles = false
        ),
        TelegramChannelItem(
            id = "tech_3",
            name = "AI Chat & Prompt Helper Bot",
            username = "ai_chat_prompt_helper_bot",
            description = "ربات دستیار گفتگو، ترجمه متن، خلاصه‌سازی اسناد و تولید کد با مدل‌های روز",
            memberCount = 210000,
            memberCountFormatted = "210K",
            category = TelegramCategory.TECH_AI,
            contentType = TelegramContentType.BOT,
            tags = listOf("ai", "bot", "chat", "tools", "smart bot"),
            isVerified = true,
            hasVideos = false,
            hasFiles = false
        ),

        // Programming
        TelegramChannelItem(
            id = "prog_1",
            name = "Python & Android Developers Hub",
            username = "python_android_dev",
            description = "آموزش‌های برنامه‌نویسی پایتون، کاتلین، فلاتر و نکات تخصصی معماری نرم‌افزار",
            memberCount = 180000,
            memberCountFormatted = "180K",
            category = TelegramCategory.PROGRAMMING,
            contentType = TelegramContentType.CHANNEL,
            tags = listOf("programming", "code", "python", "kotlin", "android", "development"),
            isVerified = true,
            hasVideos = true,
            hasFiles = true
        ),
        TelegramChannelItem(
            id = "prog_2",
            name = "Dev Books & CheatSheets PDF",
            username = "dev_books_cheatsheets",
            description = "کتاب‌های مرجع برنامه‌نویسی به زبان انگلیسی و فارسی، راهنماهای سریع و چیت‌شیت‌ها",
            memberCount = 125000,
            memberCountFormatted = "125K",
            category = TelegramCategory.PROGRAMMING,
            contentType = TelegramContentType.DOCUMENT,
            tags = listOf("programming", "books", "pdf", "file", "cheatsheet", "code"),
            isVerified = false,
            hasVideos = false,
            hasFiles = true
        ),
        TelegramChannelItem(
            id = "prog_3",
            name = "Coding Community & Q&A",
            username = "coding_questions_group",
            description = "سوپرگروه پرسش و پاسخ برنامه‌نویسان، رفع خطاهای کد و اشتراک تجربیات کاری",
            memberCount = 65000,
            memberCountFormatted = "65K",
            category = TelegramCategory.PROGRAMMING,
            contentType = TelegramContentType.GROUP,
            tags = listOf("programming", "group", "chat", "questions", "developers"),
            isVerified = false,
            hasVideos = false,
            hasFiles = true
        ),

        // Movies & Series
        TelegramChannelItem(
            id = "movie_1",
            name = "Cinema Trailers & Movie Clips",
            username = "cinema_trailers_hd",
            description = "جدیدترین تریلرها، سکانس‌های برتر فیلم‌ها و سریال‌های روز دنیا با زیرنویس چسبیده",
            memberCount = 670000,
            memberCountFormatted = "670K",
            category = TelegramCategory.MOVIES_SERIES,
            contentType = TelegramContentType.VIDEO,
            tags = listOf("movie", "series", "video", "trailer", "cinema", "clips"),
            isVerified = true,
            hasVideos = true,
            hasFiles = false
        ),
        TelegramChannelItem(
            id = "movie_2",
            name = "Movie & Series Archive DL",
            username = "movie_series_archive_dl",
            description = "آرشیو کامل فیلم‌ها و انیمیشن‌های برتر تاریخ سینما با کیفیت‌های 1080p و 720p",
            memberCount = 890000,
            memberCountFormatted = "890K",
            category = TelegramCategory.MOVIES_SERIES,
            contentType = TelegramContentType.DOCUMENT,
            tags = listOf("movie", "series", "download", "film", "mkv", "mp4", "file"),
            isVerified = true,
            hasVideos = true,
            hasFiles = true
        ),
        TelegramChannelItem(
            id = "movie_3",
            name = "IMDb & Subtitle Finder Bot",
            username = "imdb_subtitle_finder_bot",
            description = "ربات جستجوی نمرات فیلم‌ها، بیوگرافی بازیگران و دریافت فوری فایل‌های زیرنویس SRT",
            memberCount = 145000,
            memberCountFormatted = "145K",
            category = TelegramCategory.MOVIES_SERIES,
            contentType = TelegramContentType.BOT,
            tags = listOf("movie", "bot", "subtitle", "imdb", "film"),
            isVerified = false,
            hasVideos = false,
            hasFiles = false
        ),

        // Music & Podcasts
        TelegramChannelItem(
            id = "music_1",
            name = "World Hits & Trending Music",
            username = "world_hits_music",
            description = "برترین آهنگ‌های ترند جهانی، چارت‌های بیلبورد و پلی‌لیست‌های اختصاصی رادیو جوان و اسپاتیفای",
            memberCount = 490000,
            memberCountFormatted = "490K",
            category = TelegramCategory.MUSIC,
            contentType = TelegramContentType.AUDIO,
            tags = listOf("music", "song", "audio", "spotify", "hits", "track", "mp3"),
            isVerified = true,
            hasVideos = false,
            hasFiles = true
        ),
        TelegramChannelItem(
            id = "music_2",
            name = "Music Video & Concert Clips",
            username = "music_video_concerts",
            description = "موزیک ویدیوهای باکیفیت 4K و اجرای زنده کنسرت خوانندگان معروف دنیا",
            memberCount = 230000,
            memberCountFormatted = "230K",
            category = TelegramCategory.MUSIC,
            contentType = TelegramContentType.VIDEO,
            tags = listOf("music", "video", "clip", "concert", "singer"),
            isVerified = false,
            hasVideos = true,
            hasFiles = false
        ),
        TelegramChannelItem(
            id = "music_3",
            name = "Song & Shazam Downloader Bot",
            username = "song_shazam_dl_bot",
            description = "ربات شناسایی و دانلود آهنگ از روی ویس، تیک‌تاک و نام خواننده",
            memberCount = 380000,
            memberCountFormatted = "380K",
            category = TelegramCategory.MUSIC,
            contentType = TelegramContentType.BOT,
            tags = listOf("music", "bot", "shazam", "download", "mp3"),
            isVerified = true,
            hasVideos = false,
            hasFiles = false
        ),

        // Crypto & Finance
        TelegramChannelItem(
            id = "crypto_1",
            name = "Crypto Signals & Market Analysis",
            username = "crypto_signals_hub",
            description = "تحلیل‌های تکنیکال بیت‌کوین، اتریوم، آلت‌کوین‌ها و اخبار فوری بازار ارزهای دیجیتال",
            memberCount = 310000,
            memberCountFormatted = "310K",
            category = TelegramCategory.CRYPTO_FINANCE,
            contentType = TelegramContentType.CHANNEL,
            tags = listOf("crypto", "bitcoin", "trading", "finance", "signals", "market"),
            isVerified = true,
            hasVideos = false,
            hasFiles = false
        ),

        // Books & Education
        TelegramChannelItem(
            id = "book_1",
            name = "AudioBooks & Novels Library",
            username = "audiobooks_novels_lib",
            description = "کتاب‌های صوتی با صدای گویندگان برتر، رمان‌های پرفروش و پادکست‌های موفقیت فردی",
            memberCount = 275000,
            memberCountFormatted = "275K",
            category = TelegramCategory.BOOKS_EDUCATION,
            contentType = TelegramContentType.AUDIO,
            tags = listOf("book", "audiobook", "novel", "audio", "education", "podcast"),
            isVerified = true,
            hasVideos = false,
            hasFiles = true
        ),
        TelegramChannelItem(
            id = "book_2",
            name = "PDF Books & Scientific Papers",
            username = "pdf_books_scientific",
            description = "کتابخانه الکترونیکی شامل هزاران جلد کتاب تخصصی، دانشگاهی و مقالات علمی",
            memberCount = 190000,
            memberCountFormatted = "190K",
            category = TelegramCategory.BOOKS_EDUCATION,
            contentType = TelegramContentType.DOCUMENT,
            tags = listOf("book", "pdf", "file", "document", "education", "science"),
            isVerified = false,
            hasVideos = false,
            hasFiles = true
        ),

        // Wallpapers & Graphics
        TelegramChannelItem(
            id = "wall_1",
            name = "Aesthetic 4K Wallpapers & Art",
            username = "aesthetic_4k_wallpapers",
            description = "تصاویر پس‌زمینه با بالاترین وضوح، سبک‌های نئون، دارک، انیمه و طبیعت برای موبایل و تبلت",
            memberCount = 340000,
            memberCountFormatted = "340K",
            category = TelegramCategory.GRAPHICS_WALLPAPERS,
            contentType = TelegramContentType.PHOTO,
            tags = listOf("wallpaper", "photo", "art", "4k", "graphic", "dark"),
            isVerified = true,
            hasVideos = false,
            hasFiles = true
        ),

        // News & Media
        TelegramChannelItem(
            id = "news_1",
            name = "Global Breaking News & Alerts",
            username = "global_breaking_news_24",
            description = "پوشش لحظه‌ای و بی‌طرفانه اخبار مهم بین‌المللی، اقتصادی و تکنولوژی جهان",
            memberCount = 610000,
            memberCountFormatted = "610K",
            category = TelegramCategory.NEWS,
            contentType = TelegramContentType.CHANNEL,
            tags = listOf("news", "media", "breaking news", "world", "alert"),
            isVerified = true,
            hasVideos = true,
            hasFiles = false
        )
    )
}
