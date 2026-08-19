package com.xoleric.music.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents
import com.xoleric.music.ui.xolericViewModel
import com.xoleric.music.ui.screens.player.PlayerViewModel

@Composable
fun MiniPlayer(onClick: () -> Unit = {}, viewModel: PlayerViewModel = xolericViewModel()) {
    val state by viewModel.playbackState.collectAsStateWithLifecycle()
    val song = state.currentSong

    AnimatedVisibility(
        visible = song != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        if (song == null) return@AnimatedVisibility
        val progress = if (state.duration > 0) state.position.toFloat() / state.duration.toFloat() else 0f

        Column(modifier = Modifier.fillMaxWidth().zIndex(10f)) {
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(2.dp), color = LocalAccents.current.accent, trackColor = XolericColors.SurfaceVariant)
            Row(
                modifier = Modifier.fillMaxWidth().height(64.dp).background(XolericColors.MiniPlayerBg).clickable(onClick = onClick).padding(horizontal = XolericSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(XolericRadius.sm)).background(Brush.verticalGradient(listOf(XolericColors.SurfaceVariant, XolericColors.Surface))), contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Filled.Album, contentDescription = null, tint = XolericColors.TextTertiary, modifier = Modifier.size(24.dp))
                }
                Column(modifier = Modifier.weight(1f).padding(horizontal = XolericSpacing.md)) {
                    Text(text = song.title, style = MaterialTheme.typography.bodyMedium, color = XolericColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = song.artist, style = MaterialTheme.typography.bodySmall, color = XolericColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                IconButton(onClick = { viewModel.togglePlayPause() }, modifier = Modifier.size(40.dp)) {
                    Icon(imageVector = if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, contentDescription = if (state.isPlaying) "Pause" else "Play", tint = LocalAccents.current.accent, modifier = Modifier.size(28.dp))
                }
            }
        }
    }
}
