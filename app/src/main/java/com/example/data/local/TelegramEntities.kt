package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val username: String,
    val name: String,
    val description: String,
    val category: String,
    val contentType: String,
    val memberCountFormatted: String,
    val directTmeLink: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String,
    val category: String,
    val contentType: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bot_sent_history")
data class BotSentHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // TEXT, PHOTO, VIDEO, DOCUMENT, AUDIO
    val contentSummary: String,
    val fileName: String? = null,
    val fileSizeFormatted: String? = null,
    val status: String = "SUCCESS",
    val timestamp: Long = System.currentTimeMillis()
)
