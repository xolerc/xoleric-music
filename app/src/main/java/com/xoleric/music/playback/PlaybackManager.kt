package com.xoleric.music.playback

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.xoleric.music.core.model.PlaybackState
import com.xoleric.music.core.model.RepeatMode
import com.xoleric.music.core.model.ShuffleMode
import com.xoleric.music.core.model.SleepTimerState
import com.xoleric.music.core.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaybackManager constructor(
    private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var positionTracker: Job? = null
    private var sleepTimerJob: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _sleepTimerState = MutableStateFlow(SleepTimerState())
    val sleepTimerState: StateFlow<SleepTimerState> = _sleepTimerState.asStateFlow()

    private var exoPlayer: ExoPlayer? = null
    private var mediaSession: androidx.media3.session.MediaSession? = null
    private var originalQueue: List<Song> = emptyList()

    val player: ExoPlayer?
        get() = exoPlayer

    val session: androidx.media3.session.MediaSession?
        get() = mediaSession

    fun initialize() {
        if (exoPlayer != null) return

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(playerListener)
        }

        mediaSession = androidx.media3.session.MediaSession.Builder(context, exoPlayer!!).build()

        startPositionTracker()
    }

    fun release() {
        positionTracker?.cancel()
        sleepTimerJob?.cancel()
        exoPlayer?.release()
        exoPlayer = null
        mediaSession?.release()
        mediaSession = null
    }

    private fun startPositionTracker() {
        positionTracker = scope.launch {
            while (true) {
                exoPlayer?.let { player ->
                    _playbackState.update {
                        it.copy(
                            position = player.currentPosition.coerceAtLeast(0),
                            duration = player.duration.coerceAtLeast(0)
                        )
                    }
                }
                delay(200)
            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> {
                    _playbackState.update { it.copy(isLoading = false, error = null) }
                }
                Player.STATE_BUFFERING -> {
                    _playbackState.update { it.copy(isLoading = true) }
                }
                Player.STATE_ENDED -> {
                    handleTrackEnd()
                }
                Player.STATE_IDLE -> {}
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playbackState.update { it.copy(isPlaying = isPlaying) }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val queue = _playbackState.value.queue
            val newIndex = exoPlayer?.currentMediaItemIndex ?: 0
            if (newIndex in queue.indices) {
                _playbackState.update {
                    it.copy(
                        currentSong = queue[newIndex],
                        queueIndex = newIndex
                    )
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            _playbackState.update {
                it.copy(
                    isLoading = false,
                    error = "Couldn't play this song. The audio file may be unavailable or unsupported."
                )
            }
        }
    }

    private fun handleTrackEnd() {
        val state = _playbackState.value
        when (state.repeatMode) {
            RepeatMode.ONE -> {
                exoPlayer?.seekTo(0)
                exoPlayer?.play()
            }
            RepeatMode.ALL -> {
                if (state.queueIndex >= state.queue.lastIndex) {
                    playAll(state.queue, 0)
                } else {
                    next()
                }
            }
            RepeatMode.OFF -> {
                if (state.queueIndex < state.queue.lastIndex) {
                    next()
                } else {
                    _playbackState.update { it.copy(isPlaying = false) }
                }
            }
        }
    }

    fun play(song: Song) {
        initialize()
        val queue = _playbackState.value.queue.ifEmpty { listOf(song) }
        val index = queue.indexOfFirst { it.id == song.id }.coerceAtLeast(0)
        playAll(queue, index)
    }

    fun playAll(songs: List<Song>, startIndex: Int = 0) {
        if (songs.isEmpty()) return
        initialize()

        originalQueue = songs
        val mediaItems = songs.map { song ->
            MediaItem.Builder()
                .setUri(song.path)
                .setMediaId(song.id.toString())
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(song.title)
                        .setArtist(song.artist)
                        .setAlbumTitle(song.album)
                        .setArtworkUri(song.albumArtUri?.let { android.net.Uri.parse(it) })
                        .build()
                )
                .build()
        }

        exoPlayer?.apply {
            setMediaItems(mediaItems, startIndex, 0)
            prepare()
            play()
        }

        _playbackState.update {
            it.copy(
                currentSong = songs[startIndex],
                queue = songs,
                queueIndex = startIndex,
                isPlaying = true,
                isLoading = true,
                error = null
            )
        }
    }

    fun pause() {
        exoPlayer?.pause()
        _playbackState.update { it.copy(isPlaying = false) }
    }

    fun resume() {
        exoPlayer?.play()
        _playbackState.update { it.copy(isPlaying = true) }
    }

    fun togglePlayPause() {
        if (_playbackState.value.isPlaying) pause() else resume()
    }

    fun next() {
        val state = _playbackState.value
        if (state.queue.isEmpty()) return
        val nextIndex = (state.queueIndex + 1).coerceAtMost(state.queue.lastIndex)
        exoPlayer?.seekToNextMediaItem()
        _playbackState.update {
            it.copy(
                currentSong = state.queue[nextIndex],
                queueIndex = nextIndex
            )
        }
    }

    fun previous() {
        val state = _playbackState.value
        if (state.queue.isEmpty()) return
        val currentPosition = exoPlayer?.currentPosition ?: 0
        if (currentPosition > 3000) {
            seekTo(0)
            return
        }
        val prevIndex = (state.queueIndex - 1).coerceAtLeast(0)
        exoPlayer?.seekToPreviousMediaItem()
        _playbackState.update {
            it.copy(
                currentSong = state.queue[prevIndex],
                queueIndex = prevIndex
            )
        }
    }

    fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
        _playbackState.update { it.copy(position = position) }
    }

    fun toggleShuffle() {
        val current = _playbackState.value.shuffleMode
        val newMode = if (current == ShuffleMode.OFF) ShuffleMode.ON else ShuffleMode.OFF
        _playbackState.update { it.copy(shuffleMode = newMode) }

        if (newMode == ShuffleMode.ON) {
            val currentSong = _playbackState.value.currentSong ?: return
            val shuffled = _playbackState.value.queue.shuffled()
            val newIndex = shuffled.indexOfFirst { it.id == currentSong.id }.coerceAtLeast(0)
            playAll(shuffled, newIndex)
        } else {
            val currentSong = _playbackState.value.currentSong ?: return
            val restored = originalQueue.ifEmpty { _playbackState.value.queue }
            val newIndex = restored.indexOfFirst { it.id == currentSong.id }.coerceAtLeast(0)
            playAll(restored, newIndex)
        }
    }

    fun toggleRepeat() {
        val newMode = when (_playbackState.value.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        _playbackState.update { it.copy(repeatMode = newMode) }
        exoPlayer?.repeatMode = when (newMode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
        }
    }

    fun addToQueue(song: Song) {
        val state = _playbackState.value
        val newQueue = state.queue + song
        _playbackState.update { it.copy(queue = newQueue) }
        exoPlayer?.addMediaItem(
            MediaItem.Builder()
                .setUri(song.path)
                .setMediaId(song.id.toString())
                .build()
        )
    }

    fun playNext(song: Song) {
        val state = _playbackState.value
        val insertIndex = (state.queueIndex + 1).coerceAtMost(state.queue.size)
        val newQueue = state.queue.toMutableList().apply { add(insertIndex, song) }
        _playbackState.update { it.copy(queue = newQueue) }
        exoPlayer?.addMediaItem(insertIndex,
            MediaItem.Builder()
                .setUri(song.path)
                .setMediaId(song.id.toString())
                .build()
        )
    }

    fun removeFromQueue(index: Int) {
        val state = _playbackState.value
        if (index !in state.queue.indices) return
        val newQueue = state.queue.toMutableList().apply { removeAt(index) }
        val newIndex = when {
            index < state.queueIndex -> state.queueIndex - 1
            index == state.queueIndex -> state.queueIndex.coerceAtMost(newQueue.lastIndex.coerceAtLeast(0))
            else -> state.queueIndex
        }
        exoPlayer?.removeMediaItem(index)
        _playbackState.update {
            it.copy(
                queue = newQueue,
                queueIndex = newIndex,
                currentSong = newQueue.getOrNull(newIndex)
            )
        }
    }

    fun clearQueue() {
        val currentSong = _playbackState.value.currentSong
        exoPlayer?.clearMediaItems()
        if (currentSong != null) {
            _playbackState.update {
                it.copy(queue = listOf(currentSong), queueIndex = 0)
            }
        }
    }

    fun startSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        _sleepTimerState.update {
            SleepTimerState(isActive = true, remainingMinutes = minutes)
        }
        sleepTimerJob = scope.launch {
            var remaining = minutes
            while (remaining > 0) {
                delay(60_000L)
                remaining--
                _sleepTimerState.update { it.copy(remainingMinutes = remaining) }
            }
            pause()
            _sleepTimerState.update { SleepTimerState() }
        }
    }

    fun startSleepTimerEndOfSong() {
        sleepTimerJob?.cancel()
        _sleepTimerState.update {
            SleepTimerState(isActive = true, endOfSong = true, remainingMinutes = 0)
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        _sleepTimerState.update { SleepTimerState() }
    }
}
