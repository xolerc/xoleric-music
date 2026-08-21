import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';
import '../providers/player_provider.dart';
import '../utils/colors.dart';
import 'glass_card.dart';

class MiniPlayer extends StatelessWidget {
  const MiniPlayer({super.key});

  @override
  Widget build(BuildContext context) {
    final player = context.watch<PlayerProvider>();
    final song = player.currentSong;
    if (song == null) return const SizedBox.shrink();

    return GestureDetector(
      onTap: () => context.push('/player'),
      child: Container(
        height: 64,
        margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
        child: GlassCard(
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          child: Row(
            children: [
              Container(
                width: 48,
                height: 48,
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    colors: [XolericColors.neonCyan.withValues(alpha: 0.2), XolericColors.neonBlue.withValues(alpha: 0.2)],
                  ),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: const Icon(Icons.music_note, color: XolericColors.neonCyan),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(song.title, maxLines: 1, overflow: TextOverflow.ellipsis,
                        style: const TextStyle(color: XolericColors.textPrimary, fontSize: 14, fontWeight: FontWeight.w500)),
                    Text(song.artist, maxLines: 1, overflow: TextOverflow.ellipsis,
                        style: const TextStyle(color: XolericColors.textSecondary, fontSize: 12)),
                  ],
                ),
              ),
              IconButton(
                icon: Icon(player.playing ? Icons.pause_circle_filled : Icons.play_circle_fill,
                    color: XolericColors.neonCyan, size: 36),
                onPressed: player.togglePlay,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
