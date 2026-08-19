package com.xoleric.music.ui.screens.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericGlassCard
import com.xoleric.music.core.ui.components.XolericTopBar
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing

@Composable
fun AlbumsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToAlbum: (Long) -> Unit = {},
    viewModel: AlbumsViewModel = xolericViewModel()
) {
    val albums by viewModel.albums.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item { XolericTopBar(title = "Albums", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack) }
        items(albums, key = { it.id }) { album ->
            XolericGlassCard(
                modifier = Modifier.padding(XolericSpacing.sm),
                cornerRadius = XolericRadius.md,
                onClick = { onNavigateToAlbum(album.id) }
            ) {
                Column(
                    modifier = Modifier.padding(XolericSpacing.md),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(XolericRadius.sm))
                            .background(Brush.verticalGradient(listOf(XolericColors.SurfaceVariant, XolericColors.Surface))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Filled.Album, contentDescription = null, tint = XolericColors.TextTertiary, modifier = Modifier.size(48.dp))
                    }
                    Text(text = album.title, style = MaterialTheme.typography.bodyMedium, color = XolericColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = XolericSpacing.sm))
                    Text(text = album.artist, style = MaterialTheme.typography.bodySmall, color = XolericColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}
