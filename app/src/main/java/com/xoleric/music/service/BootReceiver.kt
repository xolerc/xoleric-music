package com.xoleric.music.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val prefs = context.getSharedPreferences("xoleric_prefs", Context.MODE_PRIVATE)
            val wasPlaying = prefs.getBoolean("was_playing", false)
            if (wasPlaying) {
                val serviceIntent = Intent(context, PlaybackService::class.java)
                context.startForegroundService(serviceIntent)
            }
        }
    }
}
