package com.xoleric.music.ui.screens.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.core.model.Album
import com.xoleric.music.core.model.Song
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.playback.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlbumsViewModel(
    private val musicRepository: MusicRepository,
    private val playbackManager: PlaybackManager
) : ViewModel() {
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums.asStateFlow()
    private val _albumSongs = MutableStateFlow<List<Song>>(emptyList())
    val albumSongs: StateFlow<List<Song>> = _albumSongs.asStateFlow()

    init { viewModelScope.launch { musicRepository.getAlbums().collectLatest { _albums.value = it } } }
    fun loadAlbumSongs(albumId: Long) { viewModelScope.launch { musicRepository.getSongsByAlbum(albumId).collectLatest { _albumSongs.value = it } } }
    fun playAlbum(albumId: Long) { if (_albumSongs.value.isNotEmpty()) playbackManager.playAll(_albumSongs.value, 0) }
    fun playSong(song: Song) { playbackManager.playAll(_albumSongs.value, _albumSongs.value.indexOf(song).coerceAtLeast(0)); viewModelScope.launch { musicRepository.incrementPlayCount(song.id) } }
}
