package com.xoleric.music.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.xoleric.music.core.model.Playlist
import com.xoleric.music.core.model.PlaylistSong
import com.xoleric.music.core.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists ORDER BY name ASC")
    fun getAllPlaylists(): Flow<List<Playlist>>

    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistById(id: Long): Playlist?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist): Long

    @Update
    suspend fun updatePlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylistById(id: Long)

    @Query("UPDATE playlists SET name = :name, updatedAt = :updatedAt WHERE id = :id")
    suspend fun renamePlaylist(id: Long, name: String, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT ps.songId FROM playlist_songs ps WHERE ps.playlistId = :playlistId ORDER BY ps.position ASC")
    fun getPlaylistSongIds(playlistId: Long): Flow<List<Long>>

    @Query("SELECT ps.songId FROM playlist_songs ps WHERE ps.playlistId = :playlistId ORDER BY ps.position ASC")
    suspend fun getPlaylistSongIdsList(playlistId: Long): List<Long>

    @Query("""
        SELECT s.* FROM songs s 
        INNER JOIN playlist_songs ps ON s.id = ps.songId 
        WHERE ps.playlistId = :playlistId 
        ORDER BY ps.position ASC
    """)
    fun getPlaylistSongs(playlistId: Long): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSong(playlistSong: PlaylistSong)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongs(playlistSongs: List<PlaylistSong>)

    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)

    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun clearPlaylist(playlistId: Long)

    @Query("UPDATE playlist_songs SET position = :position WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun updateSongPosition(playlistId: Long, songId: Long, position: Int)

    @Query("SELECT COUNT(*) FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun getPlaylistSongCount(playlistId: Long): Int

    @Transaction
    suspend fun reorderPlaylistSongs(playlistId: Long, songIds: List<Long>) {
        songIds.forEachIndexed { index, songId ->
            updateSongPosition(playlistId, songId, index)
        }
    }
}
