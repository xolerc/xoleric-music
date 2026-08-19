package com.xoleric.music.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LibraryViewModel(private val musicRepository: MusicRepository) : ViewModel() {
    private val _songCount = MutableStateFlow(0)
    val songCount: StateFlow<Int> = _songCount.asStateFlow()
    private val _favoriteCount = MutableStateFlow(0)
    val favoriteCount: StateFlow<Int> = _favoriteCount.asStateFlow()
    private val _albumCount = MutableStateFlow(0)
    val albumCount: StateFlow<Int> = _albumCount.asStateFlow()
    private val _artistCount = MutableStateFlow(0)
    val artistCount: StateFlow<Int> = _artistCount.asStateFlow()
    private val _folderCount = MutableStateFlow(0)
    val folderCount: StateFlow<Int> = _folderCount.asStateFlow()
    private val _genreCount = MutableStateFlow(0)
    val genreCount: StateFlow<Int> = _genreCount.asStateFlow()

    init {
        viewModelScope.launch { musicRepository.getSongCount().collectLatest { _songCount.value = it } }
        viewModelScope.launch { musicRepository.getFavoriteSongs().collectLatest { _favoriteCount.value = it.size } }
        viewModelScope.launch { musicRepository.getAlbums().collectLatest { _albumCount.value = it.size } }
        viewModelScope.launch { musicRepository.getArtists().collectLatest { _artistCount.value = it.size } }
        viewModelScope.launch { musicRepository.getFolders().collectLatest { _folderCount.value = it.size } }
        viewModelScope.launch { musicRepository.getGenres().collectLatest { _genreCount.value = it.size } }
    }
}
