import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/music_provider.dart';
import '../../providers/player_provider.dart';
import '../../widgets/empty_state.dart';

class GenresScreen extends StatelessWidget {
  const GenresScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final music = context.watch<MusicProvider>();
    final player = context.watch<PlayerProvider>();
    final genres = music.songs.fold<Map<String, List<int>>>({}, (map, song) {
      map.putIfAbsent(song.album, () => []).add(song.id);
      return map;
    });

    return Scaffold(
      appBar: AppBar(title: const Text('Genres')),
      body: genres.isEmpty
          ? const EmptyState(icon: Icons.category, message: 'No genres found')
          : ListView.builder(
              padding: const EdgeInsets.only(bottom: 80),
              itemCount: genres.length,
              itemBuilder: (ctx, i) {
                final name = genres.keys.elementAt(i);
                final ids = genres[name]!;
                return ListTile(
                  leading: Container(
                    width: 48,
                    height: 48,
                    decoration: BoxDecoration(
                      color: Colors.accents[i % Colors.accents.length].withAlpha(80),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: const Icon(Icons.category, color: Colors.white70),
                  ),
                  title: Text(name, style: const TextStyle(color: Colors.white, fontSize: 16)),
                  subtitle: Text('${ids.length} songs', style: const TextStyle(color: Colors.white54, fontSize: 13)),
                  onTap: () {
                    final songs = ids.map((id) => music.songs.firstWhere((s) => s.id == id)).toList();
                    player.playSong(songs.first, queue: songs);
                  },
                );
              },
            ),
    );
  }
}
