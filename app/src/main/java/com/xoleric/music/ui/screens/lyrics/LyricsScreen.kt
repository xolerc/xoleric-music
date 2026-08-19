package com.xoleric.music.ui.screens.lyrics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericTopBar
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.ui.screens.player.PlayerViewModel

@Composable
fun LyricsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: PlayerViewModel = xolericViewModel()
) {
    val state by viewModel.playbackState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(XolericColors.Graphite, XolericColors.Black)
                )
            )
    ) {
        XolericTopBar(
            title = "Lyrics",
            navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onNavigationClick = onNavigateBack
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No lyrics available",
                style = MaterialTheme.typography.bodyLarge,
                color = XolericColors.TextTertiary
            )
        }
    }
}
