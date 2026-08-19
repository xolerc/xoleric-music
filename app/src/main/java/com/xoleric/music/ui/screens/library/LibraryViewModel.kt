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

    init {
        viewModelScope.launch { musicRepository.getSongCount().collectLatest { _songCount.value = it } }
        viewModelScope.launch { musicRepository.getFavoriteSongs().collectLatest { _favoriteCount.value = it.size } }
    }
}
