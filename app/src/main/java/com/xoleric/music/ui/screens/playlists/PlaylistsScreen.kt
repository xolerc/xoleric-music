package com.xoleric.music.ui.screens.playlists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericGlassCard
import com.xoleric.music.core.ui.components.XolericSongRow
import com.xoleric.music.core.ui.components.XolericTopBar
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents
import com.xoleric.music.core.util.TimeUtils

@Composable
fun PlaylistsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToPlaylist: (Long) -> Unit = {},
    viewModel: PlaylistsViewModel = xolericViewModel()
) {
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item { XolericTopBar(title = "Playlists", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack) }

        item {
            Column(modifier = Modifier.padding(horizontal = XolericSpacing.lg)) {
                Text("Smart Playlists", style = MaterialTheme.typography.titleSmall, color = XolericColors.TextSecondary, modifier = Modifier.padding(vertical = XolericSpacing.sm))
                Row(horizontalArrangement = Arrangement.spacedBy(XolericSpacing.sm), modifier = Modifier.fillMaxWidth()) {
                    SmartPlaylistChip(icon = Icons.Filled.History, label = "Recently Played", modifier = Modifier.weight(1f), onClick = { onNavigateToPlaylist(-1) })
                    SmartPlaylistChip(icon = Icons.Filled.TrendingUp, label = "Most Played", modifier = Modifier.weight(1f), onClick = { onNavigateToPlaylist(-2) })
                }
                Spacer(modifier = Modifier.height(XolericSpacing.sm))
                Row(horizontalArrangement = Arrangement.spacedBy(XolericSpacing.sm), modifier = Modifier.fillMaxWidth()) {
                    SmartPlaylistChip(icon = Icons.Filled.Favorite, label = "Favorites", modifier = Modifier.weight(1f), onClick = { onNavigateToPlaylist(-3) })
                    SmartPlaylistChip(icon = Icons.Filled.MusicNote, label = "Never Played", modifier = Modifier.weight(1f), onClick = { onNavigateToPlaylist(-4) })
                }
            }
        }

        item { Spacer(modifier = Modifier.height(XolericSpacing.lg)) }

        item {
            XolericGlassCard(
                modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg),
                cornerRadius = XolericRadius.md,
                onClick = { showCreateDialog = true }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(XolericSpacing.lg),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = LocalAccents.current.accent)
                    Text(text = "Create New Playlist", style = MaterialTheme.typography.titleSmall, color = XolericColors.TextPrimary, modifier = Modifier.padding(start = XolericSpacing.md))
                }
            }
        }

        item { Spacer(modifier = Modifier.height(XolericSpacing.lg)) }

        if (playlists.isNotEmpty()) {
            item { Text("Your Playlists", style = MaterialTheme.typography.titleSmall, color = XolericColors.TextSecondary, modifier = Modifier.padding(horizontal = XolericSpacing.lg, vertical = XolericSpacing.sm)) }
        }

        items(playlists, key = { it.id }) { playlist ->
            XolericGlassCard(
                modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg, vertical = XolericSpacing.xs),
                cornerRadius = XolericRadius.md,
                onClick = { onNavigateToPlaylist(playlist.id) }
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(XolericSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.QueueMusic, contentDescription = null, tint = LocalAccents.current.accent)
                    Column(modifier = Modifier.padding(start = XolericSpacing.md).weight(1f)) {
                        Text(text = playlist.name, style = MaterialTheme.typography.bodyLarge, color = XolericColors.TextPrimary)
                        Text(text = "${playlist.songCount} songs", style = MaterialTheme.typography.bodySmall, color = XolericColors.TextSecondary)
                    }
                    IconButton(onClick = { viewModel.deletePlaylist(playlist.id) }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete", tint = XolericColors.Error)
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        var name by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            containerColor = XolericColors.SurfaceVariant,
            title = { Text("Create Playlist", color = XolericColors.TextPrimary) },
            text = {
                OutlinedTextField(
                    value = name, onValueChange = { name = it }, placeholder = { Text("Playlist name", color = XolericColors.TextTertiary) },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = XolericColors.TextPrimary, unfocusedTextColor = XolericColors.TextPrimary, focusedBorderColor = LocalAccents.current.accent, unfocusedBorderColor = XolericColors.Divider)
                )
            },
            confirmButton = { TextButton(onClick = { if (name.isNotBlank()) { viewModel.createPlaylist(name); showCreateDialog = false } }) { Text("Create", color = LocalAccents.current.accent) } },
            dismissButton = { TextButton(onClick = { showCreateDialog = false }) { Text("Cancel", color = XolericColors.TextSecondary) } }
        )
    }
}

@Composable
private fun SmartPlaylistChip(icon: ImageVector, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    XolericGlassCard(modifier = modifier.height(56.dp), cornerRadius = XolericRadius.sm, onClick = onClick) {
        Row(modifier = Modifier.fillMaxSize().padding(horizontal = XolericSpacing.md), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = LocalAccents.current.accent)
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = XolericColors.TextPrimary, modifier = Modifier.padding(start = XolericSpacing.sm))
        }
    }
}
