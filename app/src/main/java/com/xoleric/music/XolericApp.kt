package com.xoleric.music

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.xoleric.music.core.database.XolericDatabase
import com.xoleric.music.data.repository.MusicRepository
import com.xoleric.music.data.repository.PlaylistRepository
import com.xoleric.music.data.repository.QueueRepository
import com.xoleric.music.data.repository.SettingsRepository
import com.xoleric.music.playback.PlaybackManager

class XolericApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_PLAYBACK, "Playback", NotificationManager.IMPORTANCE_LOW
        ).apply { description = "Music playback controls"; setShowBadge(false) }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_PLAYBACK = "playback_channel"
    }
}

class AppContainer(context: android.content.Context) {
    val database: XolericDatabase by lazy {
        androidx.room.Room.databaseBuilder(context, XolericDatabase::class.java, "xoleric_database")
            .fallbackToDestructiveMigration().build()
    }

    val playbackManager: PlaybackManager by lazy { PlaybackManager(context) }

    val musicRepository: MusicRepository by lazy {
        MusicRepository(context, database.songDao(), database.albumDao(), database.artistDao(), database.genreDao(), database.folderDao())
    }

    val playlistRepository: PlaylistRepository by lazy {
        PlaylistRepository(database.playlistDao())
    }

    val queueRepository: QueueRepository by lazy {
        QueueRepository(database.queueDao())
    }

    val settingsRepository: SettingsRepository by lazy {
        val dataStore = androidx.datastore.preferences.core.PreferenceDataStoreFactory.create {
            context.filesDir.resolve("xoleric_settings.preferences_pb")
        }
        SettingsRepository(dataStore)
    }
}
