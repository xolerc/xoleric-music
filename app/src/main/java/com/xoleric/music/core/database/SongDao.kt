package com.xoleric.music.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.xoleric.music.core.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY title ASC")
    suspend fun getAllSongsList(): List<Song>

    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSongById(id: Long): Song?

    @Query("SELECT * FROM songs WHERE mediaStoreId = :mediaStoreId")
    suspend fun getSongByMediaStoreId(mediaStoreId: Long): Song?

    @Query("SELECT * FROM songs WHERE albumId = :albumId ORDER BY disc ASC, track ASC, title ASC")
    fun getSongsByAlbum(albumId: Long): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE artistId = :artistId ORDER BY album ASC, disc ASC, track ASC, title ASC")
    fun getSongsByArtist(artistId: Long): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE genreId = :genreId ORDER BY title ASC")
    fun getSongsByGenre(genreId: Long): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE folderPath = :folderPath ORDER BY title ASC")
    fun getSongsByFolder(folderPath: String): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE lastPlayed > 0 ORDER BY lastPlayed DESC LIMIT :limit")
    fun getRecentlyPlayed(limit: Int = 50): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY playCount DESC LIMIT :limit")
    fun getMostPlayed(limit: Int = 50): Flow<List<Song>>

    @Query("SELECT * FROM songs ORDER BY dateInserted DESC LIMIT :limit")
    fun getRecentlyAdded(limit: Int = 50): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE playCount = 0")
    fun getNeverPlayed(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE duration > :minDuration ORDER BY duration DESC")
    fun getLongTracks(minDuration: Long = 600_000): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE duration < :maxDuration ORDER BY duration ASC")
    fun getShortTracks(maxDuration: Long = 120_000): Flow<List<Song>>

    @Query("""
        SELECT * FROM songs 
        WHERE title LIKE '%' || :query || '%' 
        OR artist LIKE '%' || :query || '%' 
        OR album LIKE '%' || :query || '%'
        OR genre LIKE '%' || :query || '%'
        ORDER BY title ASC
    """)
    fun searchSongs(query: String): Flow<List<Song>>

    @Query("SELECT COUNT(*) FROM songs")
    fun getSongCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM songs")
    suspend fun getSongCountSync(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<Song>)

    @Update
    suspend fun updateSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)

    @Query("DELETE FROM songs WHERE id = :id")
    suspend fun deleteSongById(id: Long)

    @Query("UPDATE songs SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE songs SET lastPlayed = :timestamp, playCount = playCount + 1 WHERE id = :id")
    suspend fun incrementPlayCount(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()

    @Query("SELECT * FROM songs WHERE mediaStoreId IN (:mediaStoreIds)")
    suspend fun getSongsByMediaStoreIds(mediaStoreIds: List<Long>): List<Song>
}
