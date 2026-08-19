package com.xoleric.music.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xoleric.music.ui.screens.albums.AlbumDetailScreen
import com.xoleric.music.ui.screens.albums.AlbumsScreen
import com.xoleric.music.ui.screens.artists.ArtistDetailScreen
import com.xoleric.music.ui.screens.artists.ArtistsScreen
import com.xoleric.music.ui.screens.equalizer.EqualizerScreen
import com.xoleric.music.ui.screens.favorites.FavoritesScreen
import com.xoleric.music.ui.screens.folders.FolderDetailScreen
import com.xoleric.music.ui.screens.folders.FoldersScreen
import com.xoleric.music.ui.screens.genres.GenreDetailScreen
import com.xoleric.music.ui.screens.genres.GenresScreen
import com.xoleric.music.ui.screens.home.HomeScreen
import com.xoleric.music.ui.screens.library.LibraryScreen
import com.xoleric.music.ui.screens.lyrics.LyricsScreen
import com.xoleric.music.ui.screens.player.PlayerScreen
import com.xoleric.music.ui.screens.playlists.PlaylistDetailScreen
import com.xoleric.music.ui.screens.playlists.PlaylistsScreen
import com.xoleric.music.ui.screens.queue.QueueScreen
import com.xoleric.music.ui.screens.search.SearchScreen
import com.xoleric.music.ui.screens.settings.SettingsScreen
import com.xoleric.music.ui.screens.songs.SongsScreen
import java.net.URLDecoder

@Composable
fun XolericNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = { fadeIn(tween(300)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
        exitTransition = { fadeOut(tween(300)) },
        popEnterTransition = { fadeIn(tween(300)) },
        popExitTransition = { fadeOut(tween(300)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToAlbum = { navController.navigate(Screen.AlbumDetail.createRoute(it)) },
                onNavigateToArtist = { navController.navigate(Screen.ArtistDetail.createRoute(it)) },
                onNavigateToGenre = { navController.navigate(Screen.GenreDetail.createRoute(it)) },
                onNavigateToFolder = { navController.navigate(Screen.FolderDetail.createRoute(it)) },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) },
                onNavigateToSection = { route -> navController.navigate(route) }
            )
        }

        composable(Screen.Library.route) {
            LibraryScreen(
                onNavigateToSongs = { navController.navigate(Screen.Songs.route) },
                onNavigateToAlbums = { navController.navigate(Screen.Albums.route) },
                onNavigateToArtists = { navController.navigate(Screen.Artists.route) },
                onNavigateToFolders = { navController.navigate(Screen.Folders.route) },
                onNavigateToPlaylists = { navController.navigate(Screen.Playlists.route) },
                onNavigateToGenres = { navController.navigate(Screen.Genres.route) },
                onNavigateToFavorites = { navController.navigate(Screen.Favorites.route) },
                onNavigateToRecentlyPlayed = { navController.navigate(Screen.RecentlyPlayed.route) },
                onNavigateToMostPlayed = { navController.navigate(Screen.MostPlayed.route) }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) },
                onNavigateToAlbum = { navController.navigate(Screen.AlbumDetail.createRoute(it)) },
                onNavigateToArtist = { navController.navigate(Screen.ArtistDetail.createRoute(it)) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(Screen.Player.route) {
            PlayerScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToQueue = { navController.navigate(Screen.Queue.route) },
                onNavigateToLyrics = { navController.navigate(Screen.Lyrics.route) }
            )
        }

        composable(Screen.Songs.route) {
            SongsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }

        composable(Screen.Albums.route) {
            AlbumsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAlbum = { navController.navigate(Screen.AlbumDetail.createRoute(it)) }
            )
        }

        composable(
            route = Screen.AlbumDetail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.LongType })
        ) {
            AlbumDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }

        composable(Screen.Artists.route) {
            ArtistsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToArtist = { navController.navigate(Screen.ArtistDetail.createRoute(it)) }
            )
        }

        composable(
            route = Screen.ArtistDetail.route,
            arguments = listOf(navArgument("artistId") { type = NavType.LongType })
        ) {
            ArtistDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }

        composable(Screen.Folders.route) {
            FoldersScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToFolder = { navController.navigate(Screen.FolderDetail.createRoute(it)) }
            )
        }

        composable(
            route = Screen.FolderDetail.route,
            arguments = listOf(navArgument("folderPath") { type = NavType.StringType })
        ) { backStackEntry ->
            val path = URLDecoder.decode(backStackEntry.arguments?.getString("folderPath") ?: "", "UTF-8")
            FolderDetailScreen(
                folderPath = path,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }

        composable(Screen.Genres.route) {
            GenresScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToGenre = { navController.navigate(Screen.GenreDetail.createRoute(it)) }
            )
        }

        composable(
            route = Screen.GenreDetail.route,
            arguments = listOf(navArgument("genreId") { type = NavType.LongType })
        ) {
            GenreDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }

        composable(Screen.Playlists.route) {
            PlaylistsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlaylist = { navController.navigate(Screen.PlaylistDetail.createRoute(it)) }
            )
        }

        composable(
            route = Screen.PlaylistDetail.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: 0L
            PlaylistDetailScreen(
                playlistId = playlistId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }

        composable(Screen.RecentlyPlayed.route) {
            SongsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) },
                type = "recently_played"
            )
        }

        composable(Screen.MostPlayed.route) {
            SongsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) },
                type = "most_played"
            )
        }

        composable(Screen.Queue.route) {
            QueueScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayer = { navController.navigate(Screen.Player.route) }
            )
        }

        composable(Screen.Lyrics.route) {
            LyricsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Equalizer.route) {
            EqualizerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
