package com.xoleric.music.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xoleric.music.ui.xolericViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xoleric.music.core.ui.components.XolericGlassCard
import com.xoleric.music.core.ui.components.XolericSectionHeader
import com.xoleric.music.core.ui.components.XolericTopBar
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = xolericViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val accents = listOf("cyan" to "Cyan", "blue" to "Blue", "violet" to "Violet", "magenta" to "Magenta")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        XolericTopBar(title = "Settings")

        XolericSectionHeader(title = "Playback")
        XolericGlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg), cornerRadius = XolericRadius.md) {
            Column(modifier = Modifier.padding(XolericSpacing.lg)) {
                SettingsSwitch("Gapless Playback", state.gaplessEnabled) { viewModel.setGapless(it) }
                SettingsSwitch("ReplayGain", state.replayGainEnabled) { viewModel.setReplayGain(it) }
                SettingsSwitch("Pause on Disconnect", state.pauseOnDisconnect) { viewModel.setPauseOnDisconnect(it) }
            }
        }

        Spacer(modifier = Modifier.height(XolericSpacing.md))
        XolericSectionHeader(title = "Library")
        XolericGlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg), cornerRadius = XolericRadius.md) {
            Column(modifier = Modifier.padding(XolericSpacing.lg)) {
                SettingsSwitch("Auto Scan New Music", state.autoScanEnabled) { viewModel.setAutoScan(it) }
            }
        }

        Spacer(modifier = Modifier.height(XolericSpacing.md))
        XolericSectionHeader(title = "Notifications")
        XolericGlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg), cornerRadius = XolericRadius.md) {
            Column(modifier = Modifier.padding(XolericSpacing.lg)) {
                SettingsSwitch("Show Artwork", state.notificationArtwork) { viewModel.setNotificationArtwork(it) }
                SettingsSwitch("Lock Screen Controls", state.lockScreenControls) { viewModel.setLockScreenControls(it) }
            }
        }

        Spacer(modifier = Modifier.height(XolericSpacing.md))
        XolericSectionHeader(title = "Appearance")
        XolericGlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg), cornerRadius = XolericRadius.md) {
            Column(modifier = Modifier.padding(XolericSpacing.lg)) {
                Text("Accent Color", style = MaterialTheme.typography.titleSmall, color = XolericColors.TextPrimary)
                Spacer(modifier = Modifier.height(XolericSpacing.sm))
                Row(modifier = Modifier.fillMaxWidth()) {
                    accents.forEach { (name, label) ->
                        XolericGlassCard(
                            modifier = Modifier.weight(1f).padding(horizontal = XolericSpacing.xs).height(40.dp),
                            cornerRadius = XolericRadius.sm,
                            onClick = { viewModel.setAccentName(name) }
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (state.accentName == name) LocalAccents.current.accent else XolericColors.TextSecondary,
                                modifier = Modifier.padding(horizontal = XolericSpacing.xs, vertical = XolericSpacing.xs)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(XolericSpacing.md))
        XolericSectionHeader(title = "About")
        XolericGlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg), cornerRadius = XolericRadius.md) {
            Column(modifier = Modifier.padding(XolericSpacing.lg)) {
                Text("XOLERIC", style = MaterialTheme.typography.headlineSmall, color = LocalAccents.current.accent)
                Text("Version 1.0.0", style = MaterialTheme.typography.bodyMedium, color = XolericColors.TextSecondary)
                Spacer(modifier = Modifier.height(XolericSpacing.sm))
                Text("Premium Android Music Player", style = MaterialTheme.typography.bodySmall, color = XolericColors.TextTertiary)
            }
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
private fun SettingsSwitch(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = XolericSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = XolericColors.TextPrimary, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = LocalAccents.current.accent,
                checkedTrackColor = LocalAccents.current.accentAlpha040,
                uncheckedThumbColor = XolericColors.TextTertiary,
                uncheckedTrackColor = XolericColors.SurfaceVariant
            )
        )
    }
}
