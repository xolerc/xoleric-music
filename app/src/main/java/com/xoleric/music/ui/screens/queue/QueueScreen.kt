package com.xoleric.music.ui.screens.queue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericIconButton
import com.xoleric.music.core.ui.components.XolericSongRow
import com.xoleric.music.core.ui.components.XolericTopBar
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents
import com.xoleric.music.core.util.TimeUtils

@Composable
fun QueueScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToPlayer: () -> Unit = {},
    viewModel: QueueViewModel = xolericViewModel()
) {
    val state by viewModel.playbackState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            XolericTopBar(
                title = "Queue",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = onNavigateBack,
                actions = {
                    XolericIconButton(
                        icon = Icons.Filled.Delete,
                        contentDescription = "Clear Queue",
                        tint = XolericColors.Error,
                        onClick = { viewModel.clearQueue() }
                    )
                }
            )
        }

        if (state.currentSong != null) {
            item {
                Column(modifier = Modifier.padding(horizontal = XolericSpacing.lg)) {
                    Text("NOW PLAYING", style = MaterialTheme.typography.labelSmall, color = LocalAccents.current.accent)
                }
            }
            item {
                XolericSongRow(
                    title = state.currentSong!!.title,
                    artist = state.currentSong!!.artist,
                    duration = TimeUtils.formatDuration(state.currentSong!!.duration),
                    isPlaying = true,
                    onClick = onNavigateToPlayer
                )
            }
        }

        val nextSongs = state.queue.drop(state.queueIndex + 1)
        if (nextSongs.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = XolericSpacing.lg, vertical = XolericSpacing.sm)) {
                    Text("NEXT (${nextSongs.size})", style = MaterialTheme.typography.labelSmall, color = XolericColors.TextTertiary)
                }
            }
            itemsIndexed(nextSongs, key = { _, song -> "next_${song.id}_${song.path}" }) { index, song ->
                XolericSongRow(
                    title = song.title,
                    artist = song.artist,
                    duration = TimeUtils.formatDuration(song.duration),
                    showIndex = true,
                    index = index,
                    onClick = {}
                )
            }
        }

        if (state.queue.isEmpty()) {
            item {
                Text(
                    "Queue is empty",
                    style = MaterialTheme.typography.bodyLarge,
                    color = XolericColors.TextTertiary,
                    modifier = Modifier.padding(XolericSpacing.xxxl)
                )
            }
        }
    }
}
