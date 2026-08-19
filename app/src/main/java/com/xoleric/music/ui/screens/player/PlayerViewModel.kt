package com.xoleric.music.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.core.model.Song
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.playback.PlaybackManager
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playbackManager: PlaybackManager,
    private val musicRepository: MusicRepository
) : ViewModel() {
    val playbackState = playbackManager.playbackState
    val sleepTimerState = playbackManager.sleepTimerState

    fun togglePlayPause() = playbackManager.togglePlayPause()
    fun next() = playbackManager.next()
    fun previous() = playbackManager.previous()
    fun seekTo(position: Long) = playbackManager.seekTo(position)
    fun toggleShuffle() = playbackManager.toggleShuffle()
    fun toggleRepeat() = playbackManager.toggleRepeat()
    fun addToQueue(song: Song) = playbackManager.addToQueue(song)
    fun playNext(song: Song) = playbackManager.playNext(song)

    fun toggleFavorite() {
        val song = playbackState.value.currentSong ?: return
        viewModelScope.launch { musicRepository.toggleFavorite(song.id) }
    }
}
