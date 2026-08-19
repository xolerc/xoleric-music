package com.xoleric.music.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.xoleric.music.core.model.Queue
import com.xoleric.music.core.model.QueueSong
import com.xoleric.music.core.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface QueueDao {
    @Query("SELECT * FROM queue ORDER BY createdAt DESC")
    fun getAllQueues(): Flow<List<Queue>>

    @Query("SELECT * FROM queue WHERE isActive = 1 LIMIT 1")
    fun getActiveQueue(): Flow<Queue?>

    @Query("SELECT * FROM queue WHERE id = :id")
    suspend fun getQueueById(id: Long): Queue?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQueue(queue: Queue): Long

    @Update
    suspend fun updateQueue(queue: Queue)

    @Query("UPDATE queue SET isActive = 0")
    suspend fun deactivateAllQueues()

    @Query("UPDATE queue SET currentIndex = :index WHERE id = :id")
    suspend fun updateCurrentIndex(id: Long, index: Int)

    @Query("UPDATE queue SET isShuffleEnabled = :enabled WHERE id = :id")
    suspend fun updateShuffleState(id: Long, enabled: Boolean)

    @Query("DELETE FROM queue WHERE id = :id")
    suspend fun deleteQueue(id: Long)

    @Query("SELECT qs.songId FROM queue_songs qs WHERE qs.queueId = :queueId ORDER BY qs.position ASC")
    suspend fun getQueueSongIds(queueId: Long): List<Long>

    @Query("""
        SELECT s.* FROM songs s 
        INNER JOIN queue_songs qs ON s.id = qs.songId 
        WHERE qs.queueId = :queueId 
        ORDER BY qs.position ASC
    """)
    fun getQueueSongs(queueId: Long): Flow<List<Song>>

    @Query("""
        SELECT s.* FROM songs s 
        INNER JOIN queue_songs qs ON s.id = qs.songId 
        WHERE qs.queueId = :queueId 
        ORDER BY qs.position ASC
    """)
    suspend fun getQueueSongsList(queueId: Long): List<Song>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQueueSong(queueSong: QueueSong)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQueueSongs(queueSongs: List<QueueSong>)

    @Query("DELETE FROM queue_songs WHERE queueId = :queueId")
    suspend fun clearQueue(queueId: Long)

    @Query("DELETE FROM queue_songs WHERE queueId = :queueId AND songId = :songId")
    suspend fun removeSongFromQueue(queueId: Long, songId: Long)

    @Transaction
    suspend fun reorderQueueSongs(queueId: Long, songIds: List<Long>) {
        songIds.forEachIndexed { index, songId ->
            val queueSong = QueueSong(
                queueId = queueId,
                songId = songId,
                position = index
            )
            insertQueueSong(queueSong)
        }
    }
}
