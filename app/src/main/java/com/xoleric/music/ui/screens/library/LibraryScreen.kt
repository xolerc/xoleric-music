package com.xoleric.music.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericGlassCard
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents

data class LibraryItem(
    val icon: ImageVector,
    val label: String,
    val count: Int,
    val route: String
)

@Composable
fun LibraryScreen(
    onNavigateToSongs: () -> Unit = {},
    onNavigateToAlbums: () -> Unit = {},
    onNavigateToArtists: () -> Unit = {},
    onNavigateToFolders: () -> Unit = {},
    onNavigateToPlaylists: () -> Unit = {},
    onNavigateToGenres: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToRecentlyPlayed: () -> Unit = {},
    onNavigateToMostPlayed: () -> Unit = {},
    viewModel: LibraryViewModel = xolericViewModel()
) {
    val songCount by viewModel.songCount.collectAsStateWithLifecycle()
    val favoriteCount by viewModel.favoriteCount.collectAsStateWithLifecycle()
    val albumCount by viewModel.albumCount.collectAsStateWithLifecycle()
    val artistCount by viewModel.artistCount.collectAsStateWithLifecycle()
    val folderCount by viewModel.folderCount.collectAsStateWithLifecycle()
    val genreCount by viewModel.genreCount.collectAsStateWithLifecycle()

    val items = listOf(
        LibraryItem(Icons.Filled.MusicNote, "Songs", songCount, "songs"),
        LibraryItem(Icons.Filled.Album, "Albums", albumCount, "albums"),
        LibraryItem(Icons.Filled.MusicNote, "Artists", artistCount, "artists"),
        LibraryItem(Icons.Filled.Folder, "Folders", folderCount, "folders"),
        LibraryItem(Icons.Filled.QueueMusic, "Playlists", 0, "playlists"),
        LibraryItem(Icons.Filled.MusicNote, "Genres", genreCount, "genres"),
        LibraryItem(Icons.Filled.Favorite, "Favorites", favoriteCount, "favorites"),
        LibraryItem(Icons.Filled.History, "Recently Played", 0, "recently_played"),
        LibraryItem(Icons.Filled.TrendingUp, "Most Played", 0, "most_played")
    )

    val navigations = mapOf(
        "songs" to onNavigateToSongs,
        "albums" to onNavigateToAlbums,
        "artists" to onNavigateToArtists,
        "folders" to onNavigateToFolders,
        "playlists" to onNavigateToPlaylists,
        "genres" to onNavigateToGenres,
        "favorites" to onNavigateToFavorites,
        "recently_played" to onNavigateToRecentlyPlayed,
        "most_played" to onNavigateToMostPlayed
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = XolericSpacing.lg,
            vertical = XolericSpacing.lg
        ),
        horizontalArrangement = Arrangement.spacedBy(XolericSpacing.md),
        verticalArrangement = Arrangement.spacedBy(XolericSpacing.md)
    ) {
        items(items) { item ->
            XolericGlassCard(
                modifier = Modifier.height(100.dp),
                cornerRadius = XolericRadius.lg,
                onClick = { navigations[item.route]?.invoke() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(XolericSpacing.lg),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = LocalAccents.current.accent,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(XolericSpacing.sm))
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.titleSmall,
                        color = XolericColors.TextPrimary
                    )
                    Text(
                        text = if (item.count > 0) "${item.count} items" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = XolericColors.TextTertiary
                    )
                }
            }
        }
    }
}
