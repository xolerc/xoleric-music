package com.xoleric.music.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericSectionHeader
import com.xoleric.music.core.ui.components.XolericSongRow
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents
import com.xoleric.music.core.util.TimeUtils

@Composable
fun SearchScreen(
    onNavigateToPlayer: () -> Unit = {},
    onNavigateToAlbum: (Long) -> Unit = {},
    onNavigateToArtist: (Long) -> Unit = {},
    viewModel: SearchViewModel = xolericViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val songResults by viewModel.songResults.collectAsStateWithLifecycle()
    val albumResults by viewModel.albumResults.collectAsStateWithLifecycle()
    val artistResults by viewModel.artistResults.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query, onValueChange = { viewModel.onQueryChange(it) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg, vertical = XolericSpacing.sm),
            placeholder = { Text("Search songs, artists, albums…", color = XolericColors.TextTertiary) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = XolericColors.TextTertiary) },
            trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = { viewModel.onQueryChange("") }) { Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = XolericColors.TextTertiary) } },
            shape = RoundedCornerShape(XolericRadius.lg),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = XolericColors.TextPrimary, unfocusedTextColor = XolericColors.TextPrimary,
                focusedContainerColor = XolericColors.SurfaceVariant, unfocusedContainerColor = XolericColors.SurfaceVariant,
                focusedBorderColor = LocalAccents.current.accent, unfocusedBorderColor = XolericColors.Divider,
                cursorColor = LocalAccents.current.accent
            ), singleLine = true
        )

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
            if (songResults.isNotEmpty()) {
                item { XolericSectionHeader(title = "Songs (${songResults.size})") }
                items(songResults, key = { it.id }) { song ->
                    XolericSongRow(title = song.title, artist = song.artist, duration = TimeUtils.formatDuration(song.duration),
                        onClick = { viewModel.playSong(song, songResults); onNavigateToPlayer() })
                }
            }
            if (albumResults.isNotEmpty()) {
                item { XolericSectionHeader(title = "Albums (${albumResults.size})") }
                items(albumResults, key = { it.id }) { album ->
                    XolericSongRow(title = album.title, artist = "${album.artist} • ${album.songCount} songs", onClick = { onNavigateToAlbum(album.mediaStoreId) })
                }
            }
            if (artistResults.isNotEmpty()) {
                item { XolericSectionHeader(title = "Artists (${artistResults.size})") }
                items(artistResults, key = { it.id }) { artist ->
                    XolericSongRow(title = artist.name, subtitle = "${artist.songCount} songs", onClick = { onNavigateToArtist(artist.mediaStoreId) })
                }
            }
            if (query.isNotEmpty() && songResults.isEmpty() && albumResults.isEmpty() && artistResults.isEmpty()) {
                item { Text("No results found", style = MaterialTheme.typography.bodyLarge, color = XolericColors.TextTertiary, modifier = Modifier.padding(XolericSpacing.xxxl)) }
            }
        }
    }
}
