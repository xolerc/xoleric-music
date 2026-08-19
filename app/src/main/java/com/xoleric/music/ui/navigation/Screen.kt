package com.xoleric.music.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Library : Screen("library")
    data object Search : Screen("search")
    data object Settings : Screen("settings")
    data object Player : Screen("player")
    data object Songs : Screen("songs")
    data object Albums : Screen("albums")
    data object Artists : Screen("artists")
    data object Folders : Screen("folders")
    data object Playlists : Screen("playlists")
    data object Genres : Screen("genres")
    data object Favorites : Screen("favorites")
    data object Queue : Screen("queue")
    data object Lyrics : Screen("lyrics")
    data object Equalizer : Screen("equalizer")
    data object RecentlyPlayed : Screen("recently_played")
    data object MostPlayed : Screen("most_played")
    data object RecentlyAdded : Screen("recently_added")

    data object AlbumDetail : Screen("album/{albumId}") {
        fun createRoute(albumId: Long) = "album/$albumId"
    }

    data object ArtistDetail : Screen("artist/{artistId}") {
        fun createRoute(artistId: Long) = "artist/$artistId"
    }

    data object GenreDetail : Screen("genre/{genreId}") {
        fun createRoute(genreId: Long) = "genre/$genreId"
    }

    data object FolderDetail : Screen("folder/{folderPath}") {
        fun createRoute(folderPath: String) = "folder/${java.net.URLEncoder.encode(folderPath, "UTF-8")}"
    }

    data object PlaylistDetail : Screen("playlist/{playlistId}") {
        fun createRoute(playlistId: Long) = "playlist/$playlistId"
    }
}
