package com.xoleric.music.core.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "genres",
    indices = [Index(value = ["mediaStoreId"], unique = true)]
)
data class Genre(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mediaStoreId: Long,
    val name: String,
    val songCount: Int = 0
)
