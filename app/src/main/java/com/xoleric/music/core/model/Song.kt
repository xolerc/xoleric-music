package com.xoleric.music.core.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
    indices = [
        Index(value = ["mediaStoreId"], unique = true),
        Index(value = ["albumId"]),
        Index(value = ["artistId"]),
        Index(value = ["genreId"]),
        Index(value = ["folderPath"]),
        Index(value = ["title"]),
        Index(value = ["isFavorite"]),
        Index(value = ["lastPlayed"]),
        Index(value = ["playCount"])
    ]
)
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mediaStoreId: Long,
    val title: String,
    val artist: String,
    val artistId: Long = 0,
    val album: String,
    val albumId: Long = 0,
    val albumArtUri: String? = null,
    val duration: Long = 0,
    val path: String,
    val folderPath: String,
    val mimeType: String = "",
    val size: Long = 0,
    val year: Int = 0,
    val genre: String = "",
    val genreId: Long = 0,
    val track: Int = 0,
    val disc: Int = 0,
    val dateAdded: Long = 0,
    val dateModified: Long = 0,
    val isFavorite: Boolean = false,
    val lastPlayed: Long = 0,
    val playCount: Int = 0,
    val dateInserted: Long = System.currentTimeMillis()
)
