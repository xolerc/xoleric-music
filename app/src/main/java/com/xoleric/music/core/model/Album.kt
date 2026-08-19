package com.xoleric.music.core.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "albums",
    indices = [Index(value = ["mediaStoreId"], unique = true)]
)
data class Album(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mediaStoreId: Long,
    val title: String,
    val artist: String,
    val artistId: Long = 0,
    val albumArtUri: String? = null,
    val songCount: Int = 0,
    val year: Int = 0,
    val dateAdded: Long = 0
)
