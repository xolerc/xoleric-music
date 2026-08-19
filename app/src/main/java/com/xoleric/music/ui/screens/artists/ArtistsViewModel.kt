package com.xoleric.music.ui.screens.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.core.model.Artist
import com.xoleric.music.core.model.Song
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.playback.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArtistsViewModel(
    private val musicRepository: MusicRepository,
    private val playbackManager: PlaybackManager
) : ViewModel() {
    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> = _artists.asStateFlow()
    private val _artistSongs = MutableStateFlow<List<Song>>(emptyList())
    val artistSongs: StateFlow<List<Song>> = _artistSongs.asStateFlow()

    init { viewModelScope.launch { musicRepository.getArtists().collectLatest { _artists.value = it } } }
    fun loadArtistSongs(artistId: Long) { viewModelScope.launch { musicRepository.getSongsByArtist(artistId).collectLatest { _artistSongs.value = it } } }
    fun playSong(song: Song) { playbackManager.playAll(_artistSongs.value, _artistSongs.value.indexOf(song).coerceAtLeast(0)); viewModelScope.launch { musicRepository.incrementPlayCount(song.id) } }
}
