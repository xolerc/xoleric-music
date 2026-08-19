package com.xoleric.music.ui.screens.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.core.model.Folder
import com.xoleric.music.core.model.Song
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.playback.PlaybackManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FoldersViewModel(
    private val musicRepository: MusicRepository,
    private val playbackManager: PlaybackManager
) : ViewModel() {
    private val _folders = MutableStateFlow<List<Folder>>(emptyList())
    val folders: StateFlow<List<Folder>> = _folders.asStateFlow()
    private val _folderSongs = MutableStateFlow<List<Song>>(emptyList())
    val folderSongs: StateFlow<List<Song>> = _folderSongs.asStateFlow()

    init { viewModelScope.launch { musicRepository.getFolders().collectLatest { _folders.value = it } } }
    fun loadFolderSongs(path: String) { viewModelScope.launch { musicRepository.getSongsByFolder(path).collectLatest { _folderSongs.value = it } } }
    fun playSong(song: Song) { playbackManager.playAll(_folderSongs.value, _folderSongs.value.indexOf(song).coerceAtLeast(0)); viewModelScope.launch { musicRepository.incrementPlayCount(song.id) } }
}
