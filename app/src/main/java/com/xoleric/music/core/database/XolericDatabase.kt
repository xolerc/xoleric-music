package com.xoleric.music.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xoleric.music.core.model.Album
import com.xoleric.music.core.model.Artist
import com.xoleric.music.core.model.Folder
import com.xoleric.music.core.model.Genre
import com.xoleric.music.core.model.Playlist
import com.xoleric.music.core.model.PlaylistSong
import com.xoleric.music.core.model.Queue
import com.xoleric.music.core.model.QueueSong
import com.xoleric.music.core.model.Song

@Database(
    entities = [
        Song::class,
        Album::class,
        Artist::class,
        Genre::class,
        Folder::class,
        Playlist::class,
        PlaylistSong::class,
        Queue::class,
        QueueSong::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class XolericDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
    abstract fun genreDao(): GenreDao
    abstract fun folderDao(): FolderDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun queueDao(): QueueDao
}
