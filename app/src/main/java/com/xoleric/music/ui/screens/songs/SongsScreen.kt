package com.xoleric.music.ui.screens.songs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
fun SongsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToPlayer: () -> Unit = {},
    type: String = "all",
    viewModel: SongsViewModel = xolericViewModel()
) {
    val songs by viewModel.songs.collectAsStateWithLifecycle()
    val currentSongId by viewModel.currentSongId.collectAsStateWithLifecycle()

    val title = when (type) {
        "recently_played" -> "Recently Played"
        "most_played" -> "Most Played"
        "favorites" -> "Favorites"
        else -> "Songs"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                XolericTopBar(
                    title = title,
                    navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    onNavigationClick = onNavigateBack,
                    actions = {
                        XolericIconButton(
                            icon = Icons.Filled.PlayArrow,
                            contentDescription = "Play All",
                            tint = LocalAccents.current.accent,
                            onClick = { viewModel.playAll() }
                        )
                    }
                )
            }
            itemsIndexed(songs, key = { _, song -> song.id }) { index, song ->
                XolericSongRow(
                    title = song.title,
                    artist = song.artist,
                    duration = TimeUtils.formatDuration(song.duration),
                    isPlaying = currentSongId == song.id,
                    showIndex = true,
                    index = index,
                    onClick = {
                        viewModel.playSong(song)
                        onNavigateToPlayer()
                    }
                )
            }
        }
    }
}
