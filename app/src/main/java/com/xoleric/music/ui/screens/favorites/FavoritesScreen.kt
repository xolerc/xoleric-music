package com.xoleric.music.ui.screens.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericSongRow
import com.xoleric.music.core.ui.components.XolericTopBar
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.util.TimeUtils
import com.xoleric.music.ui.screens.songs.SongsViewModel

@Composable
fun FavoritesScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToPlayer: () -> Unit = {},
    viewModel: SongsViewModel = xolericViewModel()
) {
    LaunchedEffect(Unit) { viewModel.loadSongs("favorites") }
    val songs by viewModel.songs.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item { XolericTopBar(title = "Favorites", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack) }
        if (songs.isEmpty()) {
            item {
                Text(
                    text = "No favorites yet. Tap the heart icon on any song to add it here.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = XolericColors.TextTertiary,
                    modifier = Modifier.padding(XolericSpacing.xxxl)
                )
            }
        }
        itemsIndexed(songs, key = { _, song -> song.id }) { index, song ->
            XolericSongRow(
                title = song.title,
                artist = song.artist,
                duration = TimeUtils.formatDuration(song.duration),
                showIndex = true,
                index = index,
                onClick = { viewModel.playSong(song); onNavigateToPlayer() }
            )
        }
    }
}
