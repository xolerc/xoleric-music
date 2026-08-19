package com.xoleric.music.data.repository

import com.xoleric.music.core.database.QueueDao
import com.xoleric.music.core.model.Queue
import com.xoleric.music.core.model.QueueSong
import com.xoleric.music.core.model.Song
import kotlinx.coroutines.flow.Flow

class QueueRepository constructor(
    private val queueDao: QueueDao
) {
    fun getAllQueues(): Flow<List<Queue>> = queueDao.getAllQueues()
    fun getActiveQueue(): Flow<Queue?> = queueDao.getActiveQueue()
    fun getQueueSongs(queueId: Long): Flow<List<Song>> = queueDao.getQueueSongs(queueId)

    suspend fun createQueue(name: String, songIds: List<Long>): Long {
        queueDao.deactivateAllQueues()
        val queueId = queueDao.insertQueue(
            Queue(name = name, isActive = true, currentIndex = 0)
        )
        val queueSongs = songIds.mapIndexed { index, songId ->
            QueueSong(queueId = queueId, songId = songId, position = index)
        }
        queueDao.insertQueueSongs(queueSongs)
        return queueId
    }

    suspend fun deleteQueue(id: Long) {
        queueDao.deleteQueue(id)
    }

    suspend fun updateCurrentIndex(id: Long, index: Int) {
        queueDao.updateCurrentIndex(id, index)
    }

    suspend fun updateShuffleState(id: Long, enabled: Boolean) {
        queueDao.updateShuffleState(id, enabled)
    }

    suspend fun clearQueue(id: Long) {
        queueDao.clearQueue(id)
    }
}
