package com.xoleric.music.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xoleric.music.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val accentName: String = "cyan",
    val gaplessEnabled: Boolean = true,
    val crossfadeDuration: Int = 0,
    val replayGainEnabled: Boolean = false,
    val autoScanEnabled: Boolean = true,
    val notificationArtwork: Boolean = true,
    val lockScreenControls: Boolean = true,
    val pauseOnDisconnect: Boolean = true
)

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { settingsRepository.accentName.collectLatest { _uiState.update { s -> s.copy(accentName = it) } } }
        viewModelScope.launch { settingsRepository.gaplessEnabled.collectLatest { _uiState.update { s -> s.copy(gaplessEnabled = it) } } }
        viewModelScope.launch { settingsRepository.autoScanEnabled.collectLatest { _uiState.update { s -> s.copy(autoScanEnabled = it) } } }
        viewModelScope.launch { settingsRepository.notificationArtwork.collectLatest { _uiState.update { s -> s.copy(notificationArtwork = it) } } }
        viewModelScope.launch { settingsRepository.lockScreenControls.collectLatest { _uiState.update { s -> s.copy(lockScreenControls = it) } } }
        viewModelScope.launch { settingsRepository.pauseOnDisconnect.collectLatest { _uiState.update { s -> s.copy(pauseOnDisconnect = it) } } }
    }

    fun setAccentName(name: String) { viewModelScope.launch { settingsRepository.setAccentName(name) } }
    fun setGapless(enabled: Boolean) { viewModelScope.launch { settingsRepository.setGapless(enabled) } }
    fun setReplayGain(enabled: Boolean) { viewModelScope.launch { settingsRepository.setReplayGain(enabled) } }
    fun setAutoScan(enabled: Boolean) { viewModelScope.launch { settingsRepository.setAutoScan(enabled) } }
    fun setNotificationArtwork(enabled: Boolean) { viewModelScope.launch { settingsRepository.setNotificationArtwork(enabled) } }
    fun setLockScreenControls(enabled: Boolean) { viewModelScope.launch { settingsRepository.setLockScreenControls(enabled) } }
    fun setPauseOnDisconnect(enabled: Boolean) { viewModelScope.launch { settingsRepository.setPauseOnDisconnect(enabled) } }
}
