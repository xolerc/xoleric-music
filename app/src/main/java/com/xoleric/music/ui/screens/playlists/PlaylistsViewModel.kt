package com.xoleric.music.ui.screens.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.core.model.Playlist
import com.xoleric.music.core.model.Song
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.data.repository.PlaylistRepository
import com.xoleric.music.playback.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistRepository: PlaylistRepository,
    private val musicRepository: MusicRepository,
    private val playbackManager: PlaybackManager
) : ViewModel() {
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()
    private val _playlistSongs = MutableStateFlow<List<Song>>(emptyList())
    val playlistSongs: StateFlow<List<Song>> = _playlistSongs.asStateFlow()

    init { viewModelScope.launch { playlistRepository.getAllPlaylists().collectLatest { _playlists.value = it } } }
    fun loadPlaylistSongs(id: Long) { viewModelScope.launch { playlistRepository.getPlaylistSongs(id).collectLatest { _playlistSongs.value = it } } }
    fun createPlaylist(name: String) { viewModelScope.launch { playlistRepository.createPlaylist(name) } }
    fun deletePlaylist(id: Long) { viewModelScope.launch { playlistRepository.deletePlaylist(id) } }
    fun playSong(song: Song) { playbackManager.playAll(_playlistSongs.value, _playlistSongs.value.indexOf(song).coerceAtLeast(0)); viewModelScope.launch { musicRepository.incrementPlayCount(song.id) } }
    fun playAll() { if (_playlistSongs.value.isNotEmpty()) playbackManager.playAll(_playlistSongs.value, 0) }
}
