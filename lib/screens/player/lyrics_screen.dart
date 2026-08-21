import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/player_provider.dart';
import '../../utils/colors.dart';

class LyricsScreen extends StatelessWidget {
  const LyricsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final player = context.watch<PlayerProvider>();
    final song = player.currentSong;

    return Scaffold(
      appBar: AppBar(title: Text(song?.title ?? 'Lyrics')),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(32),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Icon(Icons.lyrics, size: 64, color: XolericColors.neonCyan),
              const SizedBox(height: 16),
              if (song != null) ...[
                Text(song.title, textAlign: TextAlign.center, style: const TextStyle(color: XolericColors.textPrimary, fontSize: 18, fontWeight: FontWeight.bold)),
                const SizedBox(height: 4),
                Text(song.artist, style: const TextStyle(color: XolericColors.textSecondary, fontSize: 14)),
              ],
              const SizedBox(height: 24),
              const Text('Lyrics not available', style: TextStyle(color: XolericColors.textTertiary, fontSize: 15)),
            ],
          ),
        ),
      ),
    );
  }
}
