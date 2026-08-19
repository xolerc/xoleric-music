package com.xoleric.music.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xoleric.music.core.model.Artist
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists ORDER BY name ASC")
    fun getAllArtists(): Flow<List<Artist>>

    @Query("SELECT * FROM artists WHERE id = :id")
    suspend fun getArtistById(id: Long): Artist?

    @Query("SELECT * FROM artists WHERE mediaStoreId = :mediaStoreId")
    suspend fun getArtistByMediaStoreId(mediaStoreId: Long): Artist?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: Artist): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<Artist>)

    @Query("DELETE FROM artists")
    suspend fun deleteAllArtists()

    @Query("SELECT COUNT(*) FROM artists")
    suspend fun getArtistCount(): Int
}
