package com.xoleric.music.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xoleric.music.XolericApp
import com.xoleric.music.ui.screens.albums.AlbumsViewModel
import com.xoleric.music.ui.screens.artists.ArtistsViewModel
import com.xoleric.music.ui.screens.folders.FoldersViewModel
import com.xoleric.music.ui.screens.genres.GenresViewModel
import com.xoleric.music.ui.screens.home.HomeViewModel
import com.xoleric.music.ui.screens.library.LibraryViewModel
import com.xoleric.music.ui.screens.player.PlayerViewModel
import com.xoleric.music.ui.screens.playlists.PlaylistsViewModel
import com.xoleric.music.ui.screens.queue.QueueViewModel
import com.xoleric.music.ui.screens.search.SearchViewModel
import com.xoleric.music.ui.screens.settings.SettingsViewModel
import com.xoleric.music.ui.screens.songs.SongsViewModel

class XolericViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val container = (application as XolericApp).container
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(container.musicRepository, container.playbackManager) as T
            modelClass.isAssignableFrom(PlayerViewModel::class.java) -> PlayerViewModel(container.playbackManager, container.musicRepository) as T
            modelClass.isAssignableFrom(SongsViewModel::class.java) -> SongsViewModel(container.musicRepository, container.playbackManager) as T
            modelClass.isAssignableFrom(AlbumsViewModel::class.java) -> AlbumsViewModel(container.musicRepository, container.playbackManager) as T
            modelClass.isAssignableFrom(ArtistsViewModel::class.java) -> ArtistsViewModel(container.musicRepository, container.playbackManager) as T
            modelClass.isAssignableFrom(FoldersViewModel::class.java) -> FoldersViewModel(container.musicRepository, container.playbackManager) as T
            modelClass.isAssignableFrom(GenresViewModel::class.java) -> GenresViewModel(container.musicRepository, container.playbackManager) as T
            modelClass.isAssignableFrom(PlaylistsViewModel::class.java) -> PlaylistsViewModel(container.playlistRepository, container.musicRepository, container.playbackManager) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(container.musicRepository, container.playbackManager) as T
            modelClass.isAssignableFrom(QueueViewModel::class.java) -> QueueViewModel(container.playbackManager) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(container.settingsRepository) as T
            modelClass.isAssignableFrom(LibraryViewModel::class.java) -> LibraryViewModel(container.musicRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}

@Composable
inline fun <reified T : ViewModel> xolericViewModel(): T {
    val application = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application
    val factory = XolericViewModelFactory(application)
    return viewModel(factory = factory)
}
