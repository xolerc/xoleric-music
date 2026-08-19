package com.xoleric.music.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.core.model.Album
import com.xoleric.music.core.model.Artist
import com.xoleric.music.core.model.Genre
import com.xoleric.music.core.model.Song
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.playback.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val musicRepository: MusicRepository,
    private val playbackManager: PlaybackManager
) : ViewModel() {
    private val _recentlyPlayed = MutableStateFlow<List<Song>>(emptyList())
    val recentlyPlayed: StateFlow<List<Song>> = _recentlyPlayed.asStateFlow()
    private val _recentlyAdded = MutableStateFlow<List<Song>>(emptyList())
    val recentlyAdded: StateFlow<List<Song>> = _recentlyAdded.asStateFlow()
    private val _mostPlayed = MutableStateFlow<List<Song>>(emptyList())
    val mostPlayed: StateFlow<List<Song>> = _mostPlayed.asStateFlow()
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _songCount = MutableStateFlow(0)
    val songCount: StateFlow<Int> = _songCount.asStateFlow()

    init {
        viewModelScope.launch { musicRepository.getRecentlyPlayed(10).collectLatest { _recentlyPlayed.value = it } }
        viewModelScope.launch { musicRepository.getRecentlyAdded(10).collectLatest { _recentlyAdded.value = it } }
        viewModelScope.launch { musicRepository.getMostPlayed(10).collectLatest { _mostPlayed.value = it } }
        viewModelScope.launch { musicRepository.getAlbums().collectLatest { _albums.value = it; _isLoading.value = false } }
        viewModelScope.launch { musicRepository.getSongCount().collectLatest { _songCount.value = it } }
    }

    fun playSong(song: Song) {
        val allSongs = _recentlyPlayed.value.ifEmpty { _recentlyAdded.value }.ifEmpty { _mostPlayed.value }
        if (allSongs.isNotEmpty()) playbackManager.playAll(allSongs, allSongs.indexOf(song).coerceAtLeast(0))
        else playbackManager.play(song)
        viewModelScope.launch { musicRepository.incrementPlayCount(song.id) }
    }
}
