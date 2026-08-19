package com.xoleric.music.data.repository

import android.content.Context
import com.xoleric.music.core.database.AlbumDao
import com.xoleric.music.core.database.ArtistDao
import com.xoleric.music.core.database.FolderDao
import com.xoleric.music.core.database.GenreDao
import com.xoleric.music.core.database.SongDao
import com.xoleric.music.core.model.Album
import com.xoleric.music.core.model.Artist
import com.xoleric.music.core.model.Folder
import com.xoleric.music.core.model.Genre
import com.xoleric.music.core.model.ScanState
import com.xoleric.music.core.model.Song
import com.xoleric.music.core.util.MediaStoreHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MusicRepository constructor(
    private val context: Context,
    private val songDao: SongDao,
    private val albumDao: AlbumDao,
    private val artistDao: ArtistDao,
    private val genreDao: GenreDao,
    private val folderDao: FolderDao
) {
    suspend fun scanAndSync(): ScanState = withContext(Dispatchers.IO) {
        try {
            val mediaStoreSongs = MediaStoreHelper.scanSongs(context)
            val existingSongs = songDao.getAllSongsList()
            val existingMediaStoreIds = existingSongs.map { it.mediaStoreId }.toSet()
            val newMediaStoreIds = mediaStoreSongs.map { it.mediaStoreId }.toSet()

            val toInsert = mediaStoreSongs.filter { it.mediaStoreId !in existingMediaStoreIds }
            val toDelete = existingSongs.filter { it.mediaStoreId !in newMediaStoreIds }

            if (toInsert.isNotEmpty()) {
                songDao.insertSongs(toInsert)
            }
            for (song in toDelete) {
                songDao.deleteSong(song)
            }

            val allSongs = songDao.getAllSongsList()

            val albums = MediaStoreHelper.extractAlbums(allSongs)
            val artists = MediaStoreHelper.extractArtists(allSongs)
            val folders = MediaStoreHelper.extractFolders(allSongs)
            val genres = MediaStoreHelper.extractGenres(allSongs)

            albumDao.deleteAllAlbums()
            artistDao.deleteAllArtists()
            folderDao.deleteAllFolders()
            genreDao.deleteAllGenres()

            albumDao.insertAlbums(albums)
            artistDao.insertArtists(artists)
            folderDao.insertFolders(folders)
            genreDao.insertGenres(genres)

            ScanState.Complete(allSongs.size)
        } catch (e: Exception) {
            ScanState.Error(e.message ?: "Unknown error during scan")
        }
    }

    fun getAllSongs(): Flow<List<Song>> = songDao.getAllSongs()
    fun getAlbums(): Flow<List<Album>> = albumDao.getAllAlbums()
    fun getArtists(): Flow<List<Artist>> = artistDao.getAllArtists()
    fun getGenres(): Flow<List<Genre>> = genreDao.getAllGenres()
    fun getFolders(): Flow<List<Folder>> = folderDao.getAllFolders()

    fun getSongsByAlbum(albumId: Long): Flow<List<Song>> = songDao.getSongsByAlbum(albumId)
    fun getSongsByArtist(artistId: Long): Flow<List<Song>> = songDao.getSongsByArtist(artistId)
    fun getSongsByGenre(genreId: Long): Flow<List<Song>> = songDao.getSongsByGenre(genreId)
    fun getSongsByFolder(folderPath: String): Flow<List<Song>> = songDao.getSongsByFolder(folderPath)

    fun getFavoriteSongs(): Flow<List<Song>> = songDao.getFavoriteSongs()
    fun getRecentlyPlayed(limit: Int = 50): Flow<List<Song>> = songDao.getRecentlyPlayed(limit)
    fun getMostPlayed(limit: Int = 50): Flow<List<Song>> = songDao.getMostPlayed(limit)
    fun getRecentlyAdded(limit: Int = 50): Flow<List<Song>> = songDao.getRecentlyAdded(limit)
    fun getNeverPlayed(): Flow<List<Song>> = songDao.getNeverPlayed()
    fun searchSongs(query: String): Flow<List<Song>> = songDao.searchSongs(query)

    fun getSongCount(): Flow<Int> = songDao.getSongCount()

    suspend fun toggleFavorite(songId: Long) {
        val song = songDao.getSongById(songId) ?: return
        songDao.toggleFavorite(songId, !song.isFavorite)
    }

    suspend fun incrementPlayCount(songId: Long) {
        songDao.incrementPlayCount(songId)
    }

    suspend fun getSongById(id: Long): Song? = songDao.getSongById(id)
}
