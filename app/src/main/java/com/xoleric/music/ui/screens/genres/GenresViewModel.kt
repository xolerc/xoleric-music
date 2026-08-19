package com.xoleric.music.ui.screens.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.core.model.Genre
import com.xoleric.music.core.model.Song
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.playback.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GenresViewModel(
    private val musicRepository: MusicRepository,
    private val playbackManager: PlaybackManager
) : ViewModel() {
    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()
    private val _genreSongs = MutableStateFlow<List<Song>>(emptyList())
    val genreSongs: StateFlow<List<Song>> = _genreSongs.asStateFlow()

    init { viewModelScope.launch { musicRepository.getGenres().collectLatest { _genres.value = it } } }
    fun loadGenreSongs(genreId: Long) { viewModelScope.launch { musicRepository.getSongsByGenre(genreId).collectLatest { _genreSongs.value = it } } }
    fun playSong(song: Song) { playbackManager.playAll(_genreSongs.value, _genreSongs.value.indexOf(song).coerceAtLeast(0)); viewModelScope.launch { musicRepository.incrementPlayCount(song.id) } }
}
