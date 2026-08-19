package com.xoleric.music.ui.screens.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.model.RepeatMode
import com.xoleric.music.core.model.ShuffleMode
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents
import com.xoleric.music.core.util.TimeUtils

@Composable
fun PlayerScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToQueue: () -> Unit = {},
    onNavigateToLyrics: () -> Unit = {},
    viewModel: PlayerViewModel = xolericViewModel()
) {
    val state by viewModel.playbackState.collectAsStateWithLifecycle()
    val song = state.currentSong
    val accent = LocalAccents.current.accent
    val progress = if (state.duration > 0) state.position.toFloat() / state.duration.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(200))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(XolericColors.Graphite, XolericColors.Black)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = XolericSpacing.xxxl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(XolericSpacing.xxxl))

            Text(
                text = "Now Playing",
                style = MaterialTheme.typography.labelLarge,
                color = XolericColors.TextTertiary
            )

            Spacer(modifier = Modifier.height(XolericSpacing.xxl))

            Box(
                modifier = Modifier
                    .size(280.dp)
                    .shadow(24.dp, RoundedCornerShape(XolericRadius.xxl))
                    .clip(RoundedCornerShape(XolericRadius.xxl))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(XolericColors.SurfaceVariant, XolericColors.Surface)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Album,
                    contentDescription = "Album Art",
                    tint = XolericColors.TextTertiary,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(XolericSpacing.xxxl))

            Text(
                text = song?.title ?: "No song playing",
                style = MaterialTheme.typography.headlineSmall,
                color = XolericColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(XolericSpacing.xs))
            Text(
                text = song?.artist ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = XolericColors.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(XolericSpacing.xxl))

            Slider(
                value = animatedProgress,
                onValueChange = { viewModel.seekTo((it * state.duration).toLong()) },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = accent,
                    activeTrackColor = accent,
                    inactiveTrackColor = XolericColors.SurfaceVariant
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = TimeUtils.formatDuration(state.position), style = MaterialTheme.typography.labelSmall, color = XolericColors.TextTertiary)
                Text(text = TimeUtils.formatDuration(state.duration), style = MaterialTheme.typography.labelSmall, color = XolericColors.TextTertiary)
            }

            Spacer(modifier = Modifier.height(XolericSpacing.xl))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.toggleShuffle() }) {
                    Icon(
                        imageVector = Icons.Filled.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (state.shuffleMode == ShuffleMode.ON) accent else XolericColors.TextTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { viewModel.previous() }) {
                    Icon(imageVector = Icons.Filled.SkipPrevious, contentDescription = "Previous", tint = XolericColors.TextPrimary, modifier = Modifier.size(40.dp))
                }

                IconButton(
                    onClick = { viewModel.togglePlayPause() },
                    modifier = Modifier
                        .size(64.dp)
                        .background(accent, CircleShape)
                ) {
                    Icon(
                        imageVector = if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (state.isPlaying) "Pause" else "Play",
                        tint = XolericColors.Black,
                        modifier = Modifier.size(32.dp)
                    )
                }

                IconButton(onClick = { viewModel.next() }) {
                    Icon(imageVector = Icons.Filled.SkipNext, contentDescription = "Next", tint = XolericColors.TextPrimary, modifier = Modifier.size(40.dp))
                }

                IconButton(onClick = { viewModel.toggleRepeat() }) {
                    Icon(
                        imageVector = when (state.repeatMode) { RepeatMode.ONE -> Icons.Filled.RepeatOne; else -> Icons.Filled.Repeat },
                        contentDescription = "Repeat",
                        tint = if (state.repeatMode != RepeatMode.OFF) accent else XolericColors.TextTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(XolericSpacing.xxl))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.toggleFavorite() }) {
                    Icon(
                        imageVector = if (song?.isFavorite == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (song?.isFavorite == true) XolericColors.Error else XolericColors.TextSecondary
                    )
                }
                IconButton(onClick = onNavigateToQueue) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.QueueMusic, contentDescription = "Queue", tint = XolericColors.TextSecondary)
                }
                IconButton(onClick = onNavigateToLyrics) {
                    Icon(imageVector = Icons.Filled.Assistant, contentDescription = "Lyrics", tint = XolericColors.TextSecondary)
                }
            }
        }
    }
}
