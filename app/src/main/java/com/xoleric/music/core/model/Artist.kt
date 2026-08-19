package com.xoleric.music.core.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "artists",
    indices = [Index(value = ["mediaStoreId"], unique = true)]
)
data class Artist(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mediaStoreId: Long,
    val name: String,
    val albumCount: Int = 0,
    val songCount: Int = 0,
    val albumArtUri: String? = null
)
