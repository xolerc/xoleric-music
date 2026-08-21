import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../models/song.dart';
import '../../providers/playlist_provider.dart';
import '../../providers/music_provider.dart';
import '../../providers/player_provider.dart';
import '../../widgets/song_row.dart';
import '../../widgets/empty_state.dart';

class PlaylistDetailScreen extends StatelessWidget {
  final String playlistId;
  const PlaylistDetailScreen({super.key, required this.playlistId});

  @override
  Widget build(BuildContext context) {
    final playlistProvider = context.watch<PlaylistProvider>();
    final music = context.watch<MusicProvider>();
    final playlist = playlistProvider.getById(playlistId);

    if (playlist == null) {
      return Scaffold(appBar: AppBar(), body: const Center(child: Text('Playlist not found')));
    }

    final songs = playlist.songIds
        .map((id) => music.songs.where((s) => s.id == id).firstOrNull)
        .whereType<SongModel>()
        .toList();

    return Scaffold(
      appBar: AppBar(
        title: Text(playlist.name),
        actions: [
          if (songs.isNotEmpty)
            IconButton(
              icon: const Icon(Icons.play_circle_fill, color: Color(0xFF00E5FF)),
              onPressed: () => context.read<PlayerProvider>().playSong(songs.first, queue: songs),
            ),
        ],
      ),
      body: songs.isEmpty
          ? EmptyState(
              icon: Icons.queue_music,
              message: 'No songs in this playlist.',
              actionLabel: 'Go Back',
              onAction: () => Navigator.pop(context),
            )
          : ListView.builder(
              padding: const EdgeInsets.only(bottom: 80),
              itemCount: songs.length,
              itemBuilder: (ctx, i) => SongRow(song: songs[i], queue: songs),
            ),
    );
  }
}
