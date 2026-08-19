package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TelegramDao {
    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE username = :username")
    suspend fun deleteBookmark(username: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE username = :username)")
    fun isBookmarked(username: String): Flow<Boolean>

    // Search History
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 20")
    fun getSearchHistory(): Flow<List<SearchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(item: SearchHistoryEntity)

    @Query("DELETE FROM search_history WHERE id = :id")
    suspend fun deleteSearchHistoryItem(id: Long)

    @Query("DELETE FROM search_history")
    suspend fun clearSearchHistory()

    // Bot sent history
    @Query("SELECT * FROM bot_sent_history ORDER BY timestamp DESC LIMIT 50")
    fun getBotSentHistory(): Flow<List<BotSentHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBotSentHistory(item: BotSentHistoryEntity)

    @Query("DELETE FROM bot_sent_history WHERE id = :id")
    suspend fun deleteBotSentHistoryItem(id: Long)

    @Query("DELETE FROM bot_sent_history")
    suspend fun clearBotSentHistory()
}
