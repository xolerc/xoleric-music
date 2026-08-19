package com.xoleric.music.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xoleric.music.core.model.Folder
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM folders ORDER BY name ASC")
    fun getAllFolders(): Flow<List<Folder>>

    @Query("SELECT * FROM folders WHERE path = :path")
    suspend fun getFolderByPath(path: String): Folder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: Folder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolders(folders: List<Folder>)

    @Query("DELETE FROM folders")
    suspend fun deleteAllFolders()
}
