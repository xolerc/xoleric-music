package com.xoleric.music.ui.screens.folders

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
fun FoldersScreen(onNavigateBack: () -> Unit = {}, onNavigateToFolder: (String) -> Unit = {}, viewModel: FoldersViewModel = xolericViewModel()) {
    val folders by viewModel.folders.collectAsStateWithLifecycle()
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
        item { XolericTopBar(title = "Folders", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack) }
        items(folders, key = { it.path }) { folder ->
            XolericSongRow(title = folder.name, subtitle = "${folder.songCount} songs", onClick = { onNavigateToFolder(folder.path) })
        }
    }
}

@Composable
fun FolderDetailScreen(folderPath: String = "", onNavigateBack: () -> Unit = {}, onNavigateToPlayer: () -> Unit = {}, viewModel: FoldersViewModel = xolericViewModel()) {
    LaunchedEffect(folderPath) { viewModel.loadFolderSongs(folderPath) }
    val songs by viewModel.folderSongs.collectAsStateWithLifecycle()
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
        item { XolericTopBar(title = folderPath.substringAfterLast("/"), navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack) }
        itemsIndexed(songs, key = { _, song -> song.id }) { index, song ->
            XolericSongRow(title = song.title, artist = song.artist, duration = TimeUtils.formatDuration(song.duration), showIndex = true, index = index, onClick = { viewModel.playSong(song); onNavigateToPlayer() })
        }
    }
}
