package com.xoleric.music.ui.screens.playlists

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericSongRow
import com.xoleric.music.core.ui.components.XolericTopBar
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents
import com.xoleric.music.core.util.TimeUtils

@Composable
fun PlaylistDetailScreen(
    playlistId: Long,
    onNavigateBack: () -> Unit = {},
    onNavigateToPlayer: () -> Unit = {},
    viewModel: PlaylistsViewModel = xolericViewModel()
) {
    LaunchedEffect(playlistId) { viewModel.loadPlaylistSongs(playlistId) }
    val songs by viewModel.playlistSongs.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
        item { XolericTopBar(title = "Playlist", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack, actions = {
            Button(onClick = { viewModel.playAll(); onNavigateToPlayer() }, colors = ButtonDefaults.buttonColors(containerColor = LocalAccents.current.accent, contentColor = XolericColors.Black)) {
                Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null); Text("Play", modifier = Modifier.padding(start = XolericSpacing.xs))
            }
        }) }
        itemsIndexed(songs, key = { _, song -> song.id }) { index, song ->
            XolericSongRow(title = song.title, artist = song.artist, duration = TimeUtils.formatDuration(song.duration), showIndex = true, index = index, onClick = { viewModel.playSong(song); onNavigateToPlayer() })
        }
        if (songs.isEmpty()) {
            item { Text("No songs in this playlist", style = MaterialTheme.typography.bodyLarge, color = XolericColors.TextTertiary) }
        }
    }
}
