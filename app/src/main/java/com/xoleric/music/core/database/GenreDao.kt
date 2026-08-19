package com.xoleric.music.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xoleric.music.core.model.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {
    @Query("SELECT * FROM genres ORDER BY name ASC")
    fun getAllGenres(): Flow<List<Genre>>

    @Query("SELECT * FROM genres WHERE id = :id")
    suspend fun getGenreById(id: Long): Genre?

    @Query("SELECT * FROM genres WHERE mediaStoreId = :mediaStoreId")
    suspend fun getGenreByMediaStoreId(mediaStoreId: Long): Genre?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genre: Genre): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genre>)

    @Query("DELETE FROM genres")
    suspend fun deleteAllGenres()
}
