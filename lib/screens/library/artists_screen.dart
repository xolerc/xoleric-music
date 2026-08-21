import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/music_provider.dart';
import '../../providers/player_provider.dart';
import '../../widgets/empty_state.dart';

class ArtistsScreen extends StatelessWidget {
  const ArtistsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final music = context.watch<MusicProvider>();
    final artists = music.songs.fold<Map<String, List<int>>>({}, (map, song) {
      map.putIfAbsent(song.artist, () => []).add(song.id);
      return map;
    });

    return Scaffold(
      appBar: AppBar(title: const Text('Artists')),
      body: artists.isEmpty
          ? const EmptyState(icon: Icons.person, message: 'No artists found')
          : ListView.builder(
              padding: const EdgeInsets.only(bottom: 80),
              itemCount: artists.length,
              itemBuilder: (ctx, i) {
                final name = artists.keys.elementAt(i);
                final ids = artists[name]!;
                return ListTile(
                  leading: CircleAvatar(
                    backgroundColor: Colors.primaries[i % Colors.primaries.length].withValues(alpha: 0.3),
                    child: Text(name[0].toUpperCase(), style: const TextStyle(color: Colors.white70)),
                  ),
                  title: Text(name, style: const TextStyle(color: Colors.white, fontSize: 16)),
                  subtitle: Text('${ids.length} songs', style: const TextStyle(color: Colors.white54, fontSize: 13)),
                  onTap: () {
                    final songs = ids.map((id) => music.songs.firstWhere((s) => s.id == id)).toList();
                    context.read<PlayerProvider>().playSong(songs.first, queue: songs);
                  },
                );
              },
            ),
    );
  }
}
