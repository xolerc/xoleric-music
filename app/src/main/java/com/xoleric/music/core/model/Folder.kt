package com.xoleric.music.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey val path: String,
    val name: String,
    val songCount: Int = 0,
    val dateModified: Long = 0
)
