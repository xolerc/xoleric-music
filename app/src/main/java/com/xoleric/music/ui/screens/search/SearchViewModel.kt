package com.xoleric.music.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.core.model.Album
import com.xoleric.music.core.model.Artist
import com.xoleric.music.core.model.Song
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.playback.PlaybackManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel(
    private val musicRepository: MusicRepository,
    private val playbackManager: PlaybackManager
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()
    private val _songResults = MutableStateFlow<List<Song>>(emptyList())
    val songResults: StateFlow<List<Song>> = _songResults.asStateFlow()
    private val _albumResults = MutableStateFlow<List<Album>>(emptyList())
    val albumResults: StateFlow<List<Album>> = _albumResults.asStateFlow()
    private val _artistResults = MutableStateFlow<List<Artist>>(emptyList())
    val artistResults: StateFlow<List<Artist>> = _artistResults.asStateFlow()
    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
        searchJob?.cancel()
        if (newQuery.isBlank()) { _songResults.value = emptyList(); _albumResults.value = emptyList(); _artistResults.value = emptyList(); return }
        searchJob = viewModelScope.launch {
            delay(200)
            musicRepository.searchSongs(newQuery).collectLatest { songs ->
                _songResults.value = songs
                _albumResults.value = songs.groupBy { it.albumId }.map { (_, s) -> Album(mediaStoreId = s.first().albumId, title = s.first().album, artist = s.first().artist, songCount = s.size) }.take(5)
                _artistResults.value = songs.groupBy { it.artistId }.map { (_, s) -> Artist(mediaStoreId = s.first().artistId, name = s.first().artist, songCount = s.size) }.take(5)
            }
        }
    }

    fun playSong(song: Song, allSongs: List<Song>) {
        playbackManager.playAll(allSongs, allSongs.indexOf(song).coerceAtLeast(0))
        viewModelScope.launch { musicRepository.incrementPlayCount(song.id) }
    }
}
