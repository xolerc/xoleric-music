package com.xoleric.music.ui.screens.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.core.model.Song
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.playback.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SongsViewModel(
    private val musicRepository: MusicRepository,
    private val playbackManager: PlaybackManager
) : ViewModel() {
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()
    private val _currentSongId = MutableStateFlow<Long?>(null)
    val currentSongId: StateFlow<Long?> = _currentSongId.asStateFlow()

    init {
        loadSongs()
        viewModelScope.launch { playbackManager.playbackState.collectLatest { _currentSongId.value = it.currentSong?.id } }
    }

    fun loadSongs(type: String = "all") {
        viewModelScope.launch {
            val flow = when (type) {
                "recently_played" -> musicRepository.getRecentlyPlayed()
                "most_played" -> musicRepository.getMostPlayed()
                "favorites" -> musicRepository.getFavoriteSongs()
                else -> musicRepository.getAllSongs()
            }
            flow.collectLatest { _songs.value = it }
        }
    }

    fun playSong(song: Song) {
        playbackManager.playAll(_songs.value, _songs.value.indexOf(song).coerceAtLeast(0))
        viewModelScope.launch { musicRepository.incrementPlayCount(song.id) }
    }

    fun playAll() { if (_songs.value.isNotEmpty()) playbackManager.playAll(_songs.value, 0) }
}
