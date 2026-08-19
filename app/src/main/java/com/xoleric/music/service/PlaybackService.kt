package com.xoleric.music.service

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.xoleric.music.XolericApp
import com.xoleric.music.ui.MainActivity

@UnstableApi
class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val container = (application as XolericApp).container
        container.playbackManager.initialize()

        val intent = Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_SINGLE_TOP }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        mediaSession = MediaSession.Builder(this, container.playbackManager.player!!)
            .setSessionActivity(pendingIntent).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        if (mediaSession?.player == null || mediaSession?.player?.playWhenReady == false) {
            mediaSession?.run { player.release(); release() }
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run { player.release(); release() }
        mediaSession = null
        super.onDestroy()
    }
}
