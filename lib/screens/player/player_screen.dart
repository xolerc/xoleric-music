import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/player_provider.dart';
import '../../providers/settings_provider.dart';
import '../../utils/colors.dart';
import '../../utils/constants.dart';

class PlayerScreen extends StatelessWidget {
  const PlayerScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final player = context.watch<PlayerProvider>();
    final settings = context.watch<SettingsProvider>();
    final song = player.currentSong;

    if (song == null) {
      return Scaffold(
        appBar: AppBar(),
        body: const Center(child: Text('No song playing', style: TextStyle(color: XolericColors.textTertiary))),
      );
    }

    final isFav = settings.isFavorite(song.id);
    final accent = XolericColors.fromAccentName(settings.accentColor);

    return Scaffold(
      appBar: AppBar(
        actions: [
          IconButton(icon: const Icon(Icons.queue_music), onPressed: () => Navigator.pushNamed(context, '/queue')),
        ],
      ),
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topCenter,
            end: Alignment.bottomCenter,
            colors: [accent.withValues(alpha: 0.15), XolericColors.black],
          ),
        ),
        child: Column(
          children: [
            const Spacer(flex: 2),
            Container(
              width: 280,
              height: 280,
              decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(20),
                gradient: LinearGradient(colors: [accent.withValues(alpha: 0.3), XolericColors.surface]),
                boxShadow: [BoxShadow(color: accent.withValues(alpha: 0.3), blurRadius: 40, offset: const Offset(0, 20))],
              ),
              child: const Icon(Icons.music_note, size: 80, color: XolericColors.textTertiary),
            ),
            const Spacer(),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 24),
              child: Column(
                children: [
                  Text(song.title, textAlign: TextAlign.center, maxLines: 2, overflow: TextOverflow.ellipsis,
                      style: const TextStyle(color: XolericColors.textPrimary, fontSize: 22, fontWeight: FontWeight.bold)),
                  const SizedBox(height: 4),
                  Text(song.artist, style: const TextStyle(color: XolericColors.textSecondary, fontSize: 16)),
                ],
              ),
            ),
            const SizedBox(height: 20),
            StreamBuilder<Duration>(
              stream: player.audioService.positionStream,
              builder: (ctx, snap) {
                final pos = snap.data ?? Duration.zero;
                final dur = player.duration ?? Duration.zero;
                final maxMs = dur.inMilliseconds > 0 ? dur.inMilliseconds.toDouble() : 1.0;
                return Column(
                  children: [
                    Slider(
                      value: pos.inMilliseconds.toDouble().clamp(0, maxMs),
                      max: maxMs,
                      onChanged: (v) => player.seek(Duration(milliseconds: v.toInt())),
                      activeColor: accent,
                    ),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 24),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(_fmtDur(pos), style: const TextStyle(color: XolericColors.textTertiary, fontSize: 12)),
                          Text(_fmtDur(dur), style: const TextStyle(color: XolericColors.textTertiary, fontSize: 12)),
                        ],
                      ),
                    ),
                  ],
                );
              },
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 40, vertical: 16),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  IconButton(
                    icon: Icon(player.repeatMode == AppRepeatMode.off ? Icons.repeat_outlined
                        : player.repeatMode == AppRepeatMode.all ? Icons.repeat : Icons.repeat_one,
                        color: player.repeatMode == AppRepeatMode.off ? XolericColors.textTertiary : accent),
                    onPressed: player.toggleRepeat,
                  ),
                  IconButton(
                    icon: const Icon(Icons.skip_previous, size: 32),
                    onPressed: player.hasPrevious ? player.skipPrevious : null,
                  ),
                  Container(
                    decoration: BoxDecoration(shape: BoxShape.circle, color: accent, boxShadow: [
                      BoxShadow(color: accent.withValues(alpha: 0.4), blurRadius: 20),
                    ]),
                    child: IconButton(
                      icon: Icon(player.playing ? Icons.pause : Icons.play_arrow, color: XolericColors.black, size: 36),
                      onPressed: player.togglePlay,
                    ),
                  ),
                  IconButton(
                    icon: const Icon(Icons.skip_next, size: 32),
                    onPressed: player.hasNext ? player.skipNext : null,
                  ),
                  IconButton(
                    icon: Icon(player.audioService.player.shuffleModeEnabled ? Icons.shuffle : Icons.shuffle_outlined,
                        color: player.audioService.player.shuffleModeEnabled ? accent : XolericColors.textTertiary),
                    onPressed: player.toggleShuffle,
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 60),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  IconButton(
                    icon: Icon(isFav ? Icons.favorite : Icons.favorite_border,
                        color: isFav ? XolericColors.neonMagenta : XolericColors.textTertiary),
                    onPressed: () => settings.toggleFavorite(song.id),
                  ),
                  IconButton(
                    icon: const Icon(Icons.lyrics_outlined, color: XolericColors.textTertiary),
                    onPressed: () => Navigator.pushNamed(context, '/lyrics'),
                  ),
                ],
              ),
            ),
            const Spacer(flex: 1),
          ],
        ),
      ),
    );
  }

  String _fmtDur(Duration d) {
    final m = d.inMinutes.toString().padLeft(2, '0');
    final s = (d.inSeconds % 60).toString().padLeft(2, '0');
    return '$m:$s';
  }
}
