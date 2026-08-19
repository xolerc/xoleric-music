package com.xoleric.music.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xoleric.music.core.model.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums ORDER BY title ASC")
    fun getAllAlbums(): Flow<List<Album>>

    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getAlbumById(id: Long): Album?

    @Query("SELECT * FROM albums WHERE mediaStoreId = :mediaStoreId")
    suspend fun getAlbumByMediaStoreId(mediaStoreId: Long): Album?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<Album>)

    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()

    @Query("SELECT COUNT(*) FROM albums")
    suspend fun getAlbumCount(): Int
}
