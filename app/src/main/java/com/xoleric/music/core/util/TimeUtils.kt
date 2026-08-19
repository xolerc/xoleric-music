package com.xoleric.music.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeUtils {

    fun formatDuration(millis: Long): String {
        if (millis <= 0) return "0:00"
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return if (hours > 0) {
            String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.US, "%d:%02d", minutes, seconds)
        }
    }

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    fun formatRelativeDate(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - (timestamp * 1000)
        val days = TimeUnit.MILLISECONDS.toDays(diff)
        return when {
            days == 0L -> "Today"
            days == 1L -> "Yesterday"
            days < 7L -> "$days days ago"
            days < 30L -> "${days / 7} weeks ago"
            days < 365L -> "${days / 30} months ago"
            else -> formatDate(timestamp)
        }
    }
}
