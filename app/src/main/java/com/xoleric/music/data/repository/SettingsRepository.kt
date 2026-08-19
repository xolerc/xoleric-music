package com.xoleric.music.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository constructor(
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>
) {
    companion object {
        private val KEY_ACCENT_NAME = stringPreferencesKey("accent_name")
        private val KEY_GAPLESS = booleanPreferencesKey("gapless_enabled")
        private val KEY_CROSSFADE = intPreferencesKey("crossfade_duration")
        private val KEY_REPLAY_GAIN = booleanPreferencesKey("replay_gain_enabled")
        private val KEY_AUTO_SCAN = booleanPreferencesKey("auto_scan_enabled")
        private val KEY_SCAN_FOLDERS = stringSetPreferencesKey("scan_folders")
        private val KEY_EXCLUDED_FOLDERS = stringSetPreferencesKey("excluded_folders")
        private val KEY_SORT_ORDER = stringPreferencesKey("sort_order")
        private val KEY_SORT_FIELD = stringPreferencesKey("sort_field")
        private val KEY_NOTIFICATION_ARTWORK = booleanPreferencesKey("notification_artwork")
        private val KEY_LOCK_SCREEN = booleanPreferencesKey("lock_screen_controls")
        private val KEY_PAUSE_ON_DISCONNECT = booleanPreferencesKey("pause_on_disconnect")
    }

    val accentName: Flow<String> = dataStore.data.map { it[KEY_ACCENT_NAME] ?: "cyan" }
    val gaplessEnabled: Flow<Boolean> = dataStore.data.map { it[KEY_GAPLESS] ?: true }
    val crossfadeDuration: Flow<Int> = dataStore.data.map { it[KEY_CROSSFADE] ?: 0 }
    val replayGainEnabled: Flow<Boolean> = dataStore.data.map { it[KEY_REPLAY_GAIN] ?: false }
    val autoScanEnabled: Flow<Boolean> = dataStore.data.map { it[KEY_AUTO_SCAN] ?: true }
    val sortOrder: Flow<String> = dataStore.data.map { it[KEY_SORT_ORDER] ?: "ASC" }
    val sortField: Flow<String> = dataStore.data.map { it[KEY_SORT_FIELD] ?: "TITLE" }
    val notificationArtwork: Flow<Boolean> = dataStore.data.map { it[KEY_NOTIFICATION_ARTWORK] ?: true }
    val lockScreenControls: Flow<Boolean> = dataStore.data.map { it[KEY_LOCK_SCREEN] ?: true }
    val pauseOnDisconnect: Flow<Boolean> = dataStore.data.map { it[KEY_PAUSE_ON_DISCONNECT] ?: true }

    suspend fun setAccentName(name: String) {
        dataStore.edit { it[KEY_ACCENT_NAME] = name }
    }

    suspend fun setGapless(enabled: Boolean) {
        dataStore.edit { it[KEY_GAPLESS] = enabled }
    }

    suspend fun setCrossfadeDuration(duration: Int) {
        dataStore.edit { it[KEY_CROSSFADE] = duration }
    }

    suspend fun setReplayGain(enabled: Boolean) {
        dataStore.edit { it[KEY_REPLAY_GAIN] = enabled }
    }

    suspend fun setAutoScan(enabled: Boolean) {
        dataStore.edit { it[KEY_AUTO_SCAN] = enabled }
    }

    suspend fun setNotificationArtwork(enabled: Boolean) {
        dataStore.edit { it[KEY_NOTIFICATION_ARTWORK] = enabled }
    }

    suspend fun setLockScreenControls(enabled: Boolean) {
        dataStore.edit { it[KEY_LOCK_SCREEN] = enabled }
    }

    suspend fun setPauseOnDisconnect(enabled: Boolean) {
        dataStore.edit { it[KEY_PAUSE_ON_DISCONNECT] = enabled }
    }

    suspend fun setSortOrder(order: String) {
        dataStore.edit { it[KEY_SORT_ORDER] = order }
    }

    suspend fun setSortField(field: String) {
        dataStore.edit { it[KEY_SORT_FIELD] = field }
    }
}
