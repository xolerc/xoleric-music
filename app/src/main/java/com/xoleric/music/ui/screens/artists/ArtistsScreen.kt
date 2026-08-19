package com.xoleric.music.ui.screens.artists

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.xoleric.music.core.util.TimeUtils

@Composable
fun ArtistsScreen(onNavigateBack: () -> Unit = {}, onNavigateToArtist: (Long) -> Unit = {}, viewModel: ArtistsViewModel = xolericViewModel()) {
    val artists by viewModel.artists.collectAsStateWithLifecycle()
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
        item { XolericTopBar(title = "Artists", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack) }
        items(artists, key = { it.id }) { artist ->
            XolericSongRow(title = artist.name, subtitle = "${artist.albumCount} albums \u00B7 ${artist.songCount} songs", onClick = { onNavigateToArtist(artist.mediaStoreId) })
        }
    }
}

@Composable
fun ArtistDetailScreen(artistId: Long = 0, onNavigateBack: () -> Unit = {}, onNavigateToPlayer: () -> Unit = {}, viewModel: ArtistsViewModel = xolericViewModel()) {
    LaunchedEffect(artistId) { viewModel.loadArtistSongs(artistId) }
    val songs by viewModel.artistSongs.collectAsStateWithLifecycle()
    val artistName = songs.firstOrNull()?.artist ?: "Artist"
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
        item { XolericTopBar(title = artistName, navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack) }
        itemsIndexed(songs, key = { _, song -> song.id }) { index, song ->
            XolericSongRow(title = song.title, artist = song.artist, duration = TimeUtils.formatDuration(song.duration), showIndex = true, index = index, onClick = { viewModel.playSong(song); onNavigateToPlayer() })
        }
    }
}
