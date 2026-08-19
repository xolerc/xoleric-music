package com.xoleric.music.ui.screens.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericSongRow
import com.xoleric.music.core.ui.components.XolericTopBar
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents
import com.xoleric.music.core.util.TimeUtils

@Composable
fun AlbumDetailScreen(
    albumId: Long = 0,
    onNavigateBack: () -> Unit = {},
    onNavigateToPlayer: () -> Unit = {},
    viewModel: AlbumsViewModel = xolericViewModel()
) {
    LaunchedEffect(albumId) { viewModel.loadAlbumSongs(albumId) }
    val albumSongs by viewModel.albumSongs.collectAsStateWithLifecycle()
    val albumName = albumSongs.firstOrNull()?.album ?: "Album"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            XolericTopBar(
                title = "",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = onNavigateBack
            )
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.xxl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(XolericRadius.lg))
                        .background(Brush.verticalGradient(listOf(XolericColors.SurfaceVariant, XolericColors.Surface))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Album, contentDescription = null, tint = XolericColors.TextTertiary, modifier = Modifier.size(72.dp))
                }
                Spacer(modifier = Modifier.height(XolericSpacing.lg))
                Text(text = albumName, style = MaterialTheme.typography.headlineSmall, color = XolericColors.TextPrimary)
                if (albumSongs.isNotEmpty()) {
                    Text(text = "${albumSongs.size} songs • ${albumSongs.first().artist}", style = MaterialTheme.typography.bodyMedium, color = XolericColors.TextSecondary)
                }
                Spacer(modifier = Modifier.height(XolericSpacing.lg))
                Button(
                    onClick = { viewModel.playAlbum(albumId); onNavigateToPlayer() },
                    colors = ButtonDefaults.buttonColors(containerColor = LocalAccents.current.accent, contentColor = XolericColors.Black)
                ) {
                    Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null)
                    Text(text = "Play All", modifier = Modifier.padding(start = XolericSpacing.xs))
                }
            }
        }
        itemsIndexed(albumSongs, key = { _, song -> song.id }) { index, song ->
            XolericSongRow(
                title = song.title, artist = song.artist, duration = TimeUtils.formatDuration(song.duration),
                showIndex = true, index = index,
                onClick = { viewModel.playSong(song); onNavigateToPlayer() }
            )
        }
    }
}
