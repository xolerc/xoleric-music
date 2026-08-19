package com.xoleric.music.core.util

import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.xoleric.music.core.model.Album
import com.xoleric.music.core.model.Artist
import com.xoleric.music.core.model.Folder
import com.xoleric.music.core.model.Genre
import com.xoleric.music.core.model.Song

object MediaStoreHelper {

    fun scanSongs(context: Context): List<Song> {
        val songs = mutableListOf<Song>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND ${MediaStore.Audio.Media.DURATION} > 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        context.contentResolver.query(collection, projection, selection, null, sortOrder)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val artistIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val yearColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR)
            val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(dataColumn) ?: ""
                val folderPath = path.substringBeforeLast("/", "")

                val albumArtUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    cursor.getLong(albumIdColumn)
                ).toString()

                songs.add(
                    Song(
                        mediaStoreId = id,
                        title = cursor.getString(titleColumn) ?: "Unknown",
                        artist = cursor.getString(artistColumn) ?: "Unknown",
                        artistId = cursor.getLong(artistIdColumn),
                        album = cursor.getString(albumColumn) ?: "Unknown",
                        albumId = cursor.getLong(albumIdColumn),
                        albumArtUri = albumArtUri,
                        duration = cursor.getLong(durationColumn),
                        path = path,
                        folderPath = folderPath,
                        mimeType = cursor.getString(mimeTypeColumn) ?: "",
                        size = cursor.getLong(sizeColumn),
                        year = cursor.getInt(yearColumn),
                        track = cursor.getInt(trackColumn),
                        dateAdded = cursor.getLong(dateAddedColumn),
                        dateModified = cursor.getLong(dateModifiedColumn)
                    )
                )
            }
        }
        return songs
    }

    fun extractAlbums(songs: List<Song>): List<Album> {
        return songs.groupBy { it.albumId }.map { (albumId, albumSongs) ->
            val first = albumSongs.first()
            Album(
                mediaStoreId = albumId,
                title = first.album,
                artist = first.artist,
                artistId = first.artistId,
                albumArtUri = first.albumArtUri,
                songCount = albumSongs.size,
                year = first.year
            )
        }.sortedBy { it.title }
    }

    fun extractArtists(songs: List<Song>): List<Artist> {
        return songs.groupBy { it.artistId }.map { (artistId, artistSongs) ->
            Artist(
                mediaStoreId = artistId,
                name = artistSongs.first().artist,
                albumCount = artistSongs.map { it.albumId }.distinct().size,
                songCount = artistSongs.size
            )
        }.sortedBy { it.name }
    }

    fun extractFolders(songs: List<Song>): List<Folder> {
        return songs.groupBy { it.folderPath }.map { (path, folderSongs) ->
            Folder(
                path = path,
                name = path.substringAfterLast("/", ""),
                songCount = folderSongs.size
            )
        }.sortedBy { it.name }
    }

    fun extractGenres(songs: List<Song>): List<Genre> {
        return songs.groupBy { it.genre.ifEmpty { "Unknown" } }.map { (genreName, genreSongs) ->
            Genre(
                mediaStoreId = genreSongs.hashCode().toLong(),
                name = genreName,
                songCount = genreSongs.size
            )
        }.sortedBy { it.name }
    }
}
