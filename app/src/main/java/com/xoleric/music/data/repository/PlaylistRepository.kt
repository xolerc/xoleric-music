package com.xoleric.music.data.repository

import com.xoleric.music.core.database.PlaylistDao
import com.xoleric.music.core.model.Playlist
import com.xoleric.music.core.model.PlaylistSong
import com.xoleric.music.core.model.Song
import kotlinx.coroutines.flow.Flow

class PlaylistRepository constructor(
    private val playlistDao: PlaylistDao
) {
    fun getAllPlaylists(): Flow<List<Playlist>> = playlistDao.getAllPlaylists()
    fun getPlaylistSongs(playlistId: Long): Flow<List<Song>> = playlistDao.getPlaylistSongs(playlistId)

    suspend fun createPlaylist(name: String, description: String = ""): Long {
        return playlistDao.insertPlaylist(
            Playlist(name = name, description = description)
        )
    }

    suspend fun renamePlaylist(id: Long, name: String) {
        playlistDao.renamePlaylist(id, name)
    }

    suspend fun deletePlaylist(id: Long) {
        playlistDao.deletePlaylistById(id)
    }

    suspend fun addSongsToPlaylist(playlistId: Long, songIds: List<Long>) {
        val currentCount = playlistDao.getPlaylistSongCount(playlistId)
        val songs = songIds.mapIndexed { index, songId ->
            PlaylistSong(
                playlistId = playlistId,
                songId = songId,
                position = currentCount + index
            )
        }
        playlistDao.insertPlaylistSongs(songs)
    }

    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
        playlistDao.removeSongFromPlaylist(playlistId, songId)
    }

    suspend fun reorderPlaylistSongs(playlistId: Long, songIds: List<Long>) {
        playlistDao.reorderPlaylistSongs(playlistId, songIds)
    }

    suspend fun clearPlaylist(playlistId: Long) {
        playlistDao.clearPlaylist(playlistId)
    }
}
