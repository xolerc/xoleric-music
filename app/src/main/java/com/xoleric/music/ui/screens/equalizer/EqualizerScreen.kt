package com.xoleric.music.ui.screens.equalizer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xoleric.music.core.ui.components.XolericGlassCard
import com.xoleric.music.core.ui.components.XolericTopBar
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents

private val presetNames = listOf("Flat", "Bass", "Rock", "Pop", "Classical", "Jazz", "Vocal", "Electronic", "Custom")
private val bandLabels = listOf("60Hz", "120Hz", "250Hz", "500Hz", "1kHz", "2kHz", "4kHz", "8kHz", "16kHz")

@Composable
fun EqualizerScreen(onNavigateBack: () -> Unit = {}) {
    var selectedPreset by remember { mutableFloatStateOf(0f) }

    Column(modifier = Modifier.fillMaxSize()) {
        XolericTopBar(title = "Equalizer", navigationIcon = Icons.AutoMirrored.Filled.ArrowBack, onNavigationClick = onNavigateBack)

        XolericGlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg), cornerRadius = XolericRadius.md) {
            Column(modifier = Modifier.padding(XolericSpacing.lg)) {
                Text("Preset", style = MaterialTheme.typography.titleSmall, color = XolericColors.TextPrimary)
                Spacer(modifier = Modifier.height(XolericSpacing.sm))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(XolericSpacing.sm)) {
                    presetNames.take(5).forEachIndexed { index, name ->
                        XolericGlassCard(
                            modifier = Modifier.weight(1f).height(36.dp),
                            cornerRadius = XolericRadius.sm,
                            onClick = { selectedPreset = index.toFloat() }
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selectedPreset.toInt() == index) LocalAccents.current.accent else XolericColors.TextSecondary,
                                modifier = Modifier.padding(horizontal = XolericSpacing.xs, vertical = XolericSpacing.xs)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(XolericSpacing.sm))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(XolericSpacing.sm)) {
                    presetNames.drop(5).forEachIndexed { index, name ->
                        XolericGlassCard(
                            modifier = Modifier.weight(1f).height(36.dp),
                            cornerRadius = XolericRadius.sm,
                            onClick = { selectedPreset = (index + 5).toFloat() }
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selectedPreset.toInt() == index + 5) LocalAccents.current.accent else XolericColors.TextSecondary,
                                modifier = Modifier.padding(horizontal = XolericSpacing.xs, vertical = XolericSpacing.xs)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(XolericSpacing.lg))

        XolericGlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg), cornerRadius = XolericRadius.md) {
            Column(modifier = Modifier.padding(XolericSpacing.lg)) {
                Text("Bands", style = MaterialTheme.typography.titleSmall, color = XolericColors.TextPrimary)
                Spacer(modifier = Modifier.height(XolericSpacing.sm))
                bandLabels.forEach { label ->
                    var value by remember { mutableFloatStateOf(0.5f) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = XolericColors.TextTertiary,
                            modifier = Modifier.width(40.dp)
                        )
                        Slider(
                            value = value,
                            onValueChange = { value = it },
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(
                                thumbColor = LocalAccents.current.accent,
                                activeTrackColor = LocalAccents.current.accent,
                                inactiveTrackColor = XolericColors.SurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}
