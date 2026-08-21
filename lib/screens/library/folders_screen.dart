import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/music_provider.dart';
import '../../widgets/empty_state.dart';

class FoldersScreen extends StatelessWidget {
  const FoldersScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final music = context.watch<MusicProvider>();
    final folders = music.songs.fold<Map<String, int>>({}, (map, song) {
      final folder = song.path.substring(0, song.path.lastIndexOf('/'));
      map[folder] = (map[folder] ?? 0) + 1;
      return map;
    });

    return Scaffold(
      appBar: AppBar(title: const Text('Folders')),
      body: folders.isEmpty
          ? const EmptyState(icon: Icons.folder, message: 'No folders found')
          : ListView.builder(
              padding: const EdgeInsets.only(bottom: 80),
              itemCount: folders.length,
              itemBuilder: (ctx, i) {
                final path = folders.keys.elementAt(i);
                final count = folders[path]!;
                return ListTile(
                  leading: const Icon(Icons.folder, color: Color(0xFFFFCA28), size: 32),
                  title: Text(path.split('/').last, style: const TextStyle(color: Colors.white, fontSize: 16)),
                  subtitle: Text('$count songs', style: const TextStyle(color: Colors.white54, fontSize: 13)),
                );
              },
            ),
    );
  }
}
