package com.xoleric.music.core.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "queue_songs",
    primaryKeys = ["queueId", "songId", "position"],
    foreignKeys = [
        ForeignKey(
            entity = Queue::class,
            parentColumns = ["id"],
            childColumns = ["queueId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["queueId"]),
        Index(value = ["songId"])
    ]
)
data class QueueSong(
    val queueId: Long,
    val songId: Long,
    val position: Int,
    val dateAdded: Long = System.currentTimeMillis()
)
