package com.xoleric.music.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.model.Album
import com.xoleric.music.core.model.Song
import com.xoleric.music.core.ui.components.XolericGlassCard
import com.xoleric.music.core.ui.components.XolericSectionHeader
import com.xoleric.music.core.ui.components.XolericSongRow
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents
import com.xoleric.music.core.util.TimeUtils
import com.xoleric.music.ui.navigation.Screen
import java.util.Calendar

@Composable
fun HomeScreen(
    onNavigateToAlbum: (Long) -> Unit = {},
    onNavigateToArtist: (Long) -> Unit = {},
    onNavigateToGenre: (Long) -> Unit = {},
    onNavigateToFolder: (String) -> Unit = {},
    onNavigateToPlayer: () -> Unit = {},
    onNavigateToSection: (String) -> Unit = {},
    viewModel: HomeViewModel = xolericViewModel()
) {
    val recentlyPlayed by viewModel.recentlyPlayed.collectAsStateWithLifecycle()
    val recentlyAdded by viewModel.recentlyAdded.collectAsStateWithLifecycle()
    val mostPlayed by viewModel.mostPlayed.collectAsStateWithLifecycle()
    val albums by viewModel.albums.collectAsStateWithLifecycle()
    val artists by viewModel.artists.collectAsStateWithLifecycle()
    val folders by viewModel.folders.collectAsStateWithLifecycle()
    val genres by viewModel.genres.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = LocalAccents.current.accent)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = XolericSpacing.lg, vertical = XolericSpacing.xxxl)
            ) {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.headlineLarge,
                    color = XolericColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(XolericSpacing.xs))
                Text(
                    text = "Your music, your space.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = XolericColors.TextSecondary
                )
            }
        }

        if (recentlyPlayed.isNotEmpty()) {
            item {
                XolericSectionHeader(
                    title = "Continue Listening",
                    action = "See All",
                    onActionClick = { onNavigateToSection(Screen.RecentlyPlayed.route) }
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = XolericSpacing.lg),
                    horizontalArrangement = Arrangement.spacedBy(XolericSpacing.md)
                ) {
                    items(recentlyPlayed, key = { it.id }) { song ->
                        AlbumCard(
                            title = song.title,
                            subtitle = song.artist,
                            onClick = {
                                viewModel.playSong(song)
                                onNavigateToPlayer()
                            }
                        )
                    }
                }
            }
        }

        if (recentlyAdded.isNotEmpty()) {
            item {
                XolericSectionHeader(
                    title = "Recently Added",
                    action = "See All"
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = XolericSpacing.lg),
                    horizontalArrangement = Arrangement.spacedBy(XolericSpacing.md)
                ) {
                    items(recentlyAdded, key = { it.id }) { song ->
                        AlbumCard(
                            title = song.title,
                            subtitle = song.artist,
                            onClick = {
                                viewModel.playSong(song)
                                onNavigateToPlayer()
                            }
                        )
                    }
                }
            }
        }

        if (mostPlayed.isNotEmpty()) {
            item {
                XolericSectionHeader(
                    title = "Most Played",
                    action = "See All",
                    onActionClick = { onNavigateToSection(Screen.MostPlayed.route) }
                )
            }
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = XolericSpacing.lg),
                    horizontalArrangement = Arrangement.spacedBy(XolericSpacing.md)
                ) {
                    items(mostPlayed, key = { it.id }) { song ->
                        AlbumCard(
                            title = song.title,
                            subtitle = song.artist,
                            onClick = {
                                viewModel.playSong(song)
                                onNavigateToPlayer()
                            }
                        )
                    }
                }
            }
        }

        item {
            XolericSectionHeader(title = "Your Library")
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = XolericSpacing.lg),
                horizontalArrangement = Arrangement.spacedBy(XolericSpacing.md)
            ) {
                LibraryQuickItem(
                    icon = Icons.Filled.Album,
                    label = "Albums",
                    count = albums.size,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToSection(Screen.Albums.route) }
                )
                LibraryQuickItem(
                    icon = Icons.Filled.MusicNote,
                    label = "Artists",
                    count = artists.size,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToSection(Screen.Artists.route) }
                )
                LibraryQuickItem(
                    icon = Icons.Filled.Folder,
                    label = "Folders",
                    count = folders.size,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToSection(Screen.Folders.route) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(XolericSpacing.md))
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = XolericSpacing.lg),
                horizontalArrangement = Arrangement.spacedBy(XolericSpacing.md)
            ) {
                LibraryQuickItem(
                    icon = Icons.Filled.QueueMusic,
                    label = "Playlists",
                    count = genres.size,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToSection(Screen.Playlists.route) }
                )
                LibraryQuickItem(
                    icon = Icons.Filled.MusicNote,
                    label = "Genres",
                    count = genres.size,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToSection(Screen.Genres.route) }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun AlbumCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    XolericGlassCard(
        modifier = Modifier.width(140.dp),
        cornerRadius = XolericRadius.md,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(XolericSpacing.md)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(XolericRadius.sm))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                XolericColors.SurfaceVariant,
                                XolericColors.Surface
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Album,
                    contentDescription = null,
                    tint = XolericColors.TextTertiary,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(XolericSpacing.sm))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = XolericColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = XolericColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LibraryQuickItem(
    icon: ImageVector,
    label: String,
    count: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    XolericGlassCard(
        modifier = modifier.height(90.dp),
        cornerRadius = XolericRadius.md,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(XolericSpacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = LocalAccents.current.accent,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(XolericSpacing.xs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = XolericColors.TextPrimary
            )
        }
    }
}
