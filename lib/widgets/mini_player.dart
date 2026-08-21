import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/player_provider.dart';
import '../providers/theme_provider.dart';
import '../screens/player/player_screen.dart';

class MiniPlayer extends StatelessWidget {
  const MiniPlayer({super.key});

  @override
  Widget build(BuildContext context) {
    final player = context.watch<PlayerProvider>();
    final t = context.watch<ThemeProvider>().current;
    final song = player.currentSong;

    if (song == null) return const SizedBox.shrink();

    return GestureDetector(
      onTap: () => Navigator.push(
        context,
        PageRouteBuilder(
          pageBuilder: (_, __, ___) => const PlayerScreen(),
          transitionsBuilder: (_, anim, __, child) =>
              SlideTransition(position: Tween<Offset>(
                begin: const Offset(0, 1), end: Offset.zero,
              ).animate(CurvedAnimation(parent: anim, curve: Curves.easeOutCubic)),
              child: child),
        ),
      ),
      child: Container(
        margin: const EdgeInsets.fromLTRB(16, 0, 16, 8),
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
        decoration: BoxDecoration(
          color: t.surface,
          borderRadius: BorderRadius.circular(t.cardRadius),
          border: Border.all(color: t.divider, width: 0.5),
          boxShadow: [
            BoxShadow(color: t.accent.withAlpha(20), blurRadius: 20, offset: const Offset(0, 4)),
          ],
        ),
        child: Row(
          children: [
            Container(
              width: 44, height: 44,
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [t.accent.withAlpha(80), t.accentSecondary.withAlpha(80)],
                ),
                borderRadius: BorderRadius.circular(10),
              ),
              child: Center(
                child: Icon(Icons.music_note, color: t.textPrimary, size: 22),
              ),
            ),
            const SizedBox(width: 12),
            Expanded(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(song.title, maxLines: 1, overflow: TextOverflow.ellipsis,
                    style: TextStyle(color: t.textPrimary, fontSize: 14, fontWeight: FontWeight.w500)),
                  const SizedBox(height: 2),
                  Text(song.artist, maxLines: 1, overflow: TextOverflow.ellipsis,
                    style: TextStyle(color: t.textTertiary, fontSize: 12)),
                ],
              ),
            ),
            IconButton(
              icon: Icon(Icons.skip_previous, color: t.textPrimary, size: 24),
              onPressed: player.hasPrevious ? () => player.skipPrevious() : null,
              padding: EdgeInsets.zero,
              constraints: const BoxConstraints(minWidth: 36, minHeight: 36),
            ),
            Container(
              width: 40, height: 40,
              decoration: BoxDecoration(
                color: t.accent, shape: BoxShape.circle,
              ),
              child: IconButton(
                icon: Icon(
                  player.playing ? Icons.pause_rounded : Icons.play_arrow_rounded,
                  color: t.background, size: 24,
                ),
                onPressed: () => player.togglePlay(),
                padding: EdgeInsets.zero,
              ),
            ),
            IconButton(
              icon: Icon(Icons.skip_next, color: t.textPrimary, size: 24),
              onPressed: player.hasNext ? () => player.skipNext() : null,
              padding: EdgeInsets.zero,
              constraints: const BoxConstraints(minWidth: 36, minHeight: 36),
            ),
          ],
        ),
      ),
    );
  }
}
