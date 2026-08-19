package com.xoleric.music.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queue")
data class Queue(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val currentIndex: Int = 0,
    val isShuffleEnabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = false
)
