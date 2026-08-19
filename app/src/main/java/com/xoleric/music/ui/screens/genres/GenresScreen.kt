package com.xoleric.music.ui.screens.genres

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
fun GenresScreen(onNavigateBack: () -> Unit = {}, onNavigateToGenre: (Long) -> Unit = {}, viewModel: GenresViewModel = xolericViewModel()) {
    val genres by viewModel.genres.collectAsStateWithLifecycle()
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
        item { XolericTopBar(title = "Genres", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack) }
        items(genres, key = { it.id }) { genre ->
            XolericSongRow(title = genre.name, subtitle = "${genre.songCount} songs", onClick = { onNavigateToGenre(genre.id) })
        }
    }
}

@Composable
fun GenreDetailScreen(genreId: Long = 0, onNavigateBack: () -> Unit = {}, onNavigateToPlayer: () -> Unit = {}, viewModel: GenresViewModel = xolericViewModel()) {
    LaunchedEffect(genreId) { viewModel.loadGenreSongs(genreId) }
    val songs by viewModel.genreSongs.collectAsStateWithLifecycle()
    val genreName = songs.firstOrNull()?.genre ?: "Genre"
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
        item { XolericTopBar(title = genreName, navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack) }
        itemsIndexed(songs, key = { _, song -> song.id }) { index, song ->
            XolericSongRow(title = song.title, artist = song.artist, duration = TimeUtils.formatDuration(song.duration), showIndex = true, index = index, onClick = { viewModel.playSong(song); onNavigateToPlayer() })
        }
    }
}
