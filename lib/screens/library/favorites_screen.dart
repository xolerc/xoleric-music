import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/music_provider.dart';
import '../../providers/settings_provider.dart';
import '../../widgets/song_row.dart';
import '../../widgets/empty_state.dart';

class FavoritesScreen extends StatelessWidget {
  const FavoritesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final music = context.watch<MusicProvider>();
    final settings = context.watch<SettingsProvider>();
    final favSongs = music.songs.where((s) => settings.isFavorite(s.id)).toList();

    return Scaffold(
      appBar: AppBar(title: const Text('Favorites')),
      body: favSongs.isEmpty
          ? const EmptyState(icon: Icons.favorite_border, message: 'No favorites yet.\nTap the heart icon to add songs.')
          : ListView.builder(
              padding: const EdgeInsets.only(bottom: 80),
              itemCount: favSongs.length,
              itemBuilder: (ctx, i) => SongRow(song: favSongs[i], queue: favSongs),
            ),
    );
  }
}
