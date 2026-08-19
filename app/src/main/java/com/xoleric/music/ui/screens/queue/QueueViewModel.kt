package com.xoleric.music.ui.screens.queue

import androidx.lifecycle.ViewModel
import com.xoleric.music.core.model.Song
import com.xoleric.music.playback.PlaybackManager

class QueueViewModel(private val playbackManager: PlaybackManager) : ViewModel() {
    val playbackState = playbackManager.playbackState
    fun removeFromQueue(index: Int) = playbackManager.removeFromQueue(index)
    fun clearQueue() = playbackManager.clearQueue()
}
