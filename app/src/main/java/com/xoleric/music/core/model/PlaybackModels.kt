package com.xoleric.music.core.model

enum class RepeatMode {
    OFF, ONE, ALL
}

enum class ShuffleMode {
    OFF, ON
}

enum class SortOrder {
    ASC, DESC
}

enum class SortField {
    TITLE, ARTIST, ALBUM, DATE_ADDED, DATE_MODIFIED, DURATION, TRACK, MOST_PLAYED, LAST_PLAYED
}

data class PlaybackState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val position: Long = 0,
    val duration: Long = 0,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleMode: ShuffleMode = ShuffleMode.OFF,
    val queue: List<Song> = emptyList(),
    val queueIndex: Int = -1,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class QueueState(
    val id: Long = 0,
    val name: String = "Queue",
    val songs: List<Song> = emptyList(),
    val currentIndex: Int = 0,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF
)

data class SleepTimerState(
    val isActive: Boolean = false,
    val remainingMinutes: Int = 0,
    val endOfSong: Boolean = false
)

data class EqualizerPreset(
    val name: String,
    val bands: List<Float>
)

sealed class ScanState {
    data object Idle : ScanState()
    data object Scanning : ScanState()
    data class Progress(val found: Int) : ScanState()
    data class Complete(val total: Int) : ScanState()
    data class Error(val message: String) : ScanState()
}
