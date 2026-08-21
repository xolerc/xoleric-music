import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/player_provider.dart';
import '../../providers/theme_provider.dart';
import '../../utils/constants.dart';

class PlayerScreen extends StatefulWidget {
  const PlayerScreen({super.key});
  @override
  State<PlayerScreen> createState() => _PlayerScreenState();
}

class _PlayerScreenState extends State<PlayerScreen> {
  @override
  Widget build(BuildContext context) {
    final player = context.watch<PlayerProvider>();
    final t = context.watch<ThemeProvider>().current;
    final song = player.currentSong;

    return Scaffold(
      backgroundColor: t.background,
      body: Stack(
        children: [
          Positioned.fill(
            child: Container(
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topCenter, end: Alignment.bottomCenter,
                  colors: [t.accent.withAlpha(40), t.background],
                ),
              ),
            ),
          ),
          if (t.useGlass)
            BackdropFilter(
              filter: ImageFilter.blur(sigmaX: 50, sigmaY: 50),
              child: Container(color: t.background.withAlpha(180)),
            ),
          SafeArea(
            child: Column(
              children: [
                _buildTopBar(t, context),
                const Spacer(),
                _buildAlbumArt(t, song),
                const Spacer(),
                _buildSongInfo(song, t),
                const SizedBox(height: 24),
                _buildProgressBar(player, t),
                const SizedBox(height: 16),
                _buildControls(player, t),
                const SizedBox(height: 24),
                _buildSecondaryControls(player, t),
                const SizedBox(height: 32),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildTopBar(dynamic t, BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: Row(
        children: [
          IconButton(
            icon: Icon(Icons.keyboard_arrow_down, color: t.textPrimary, size: 32),
            onPressed: () => Navigator.pop(context),
          ),
          Expanded(
            child: Column(
              children: [
                Text('PLAYING FROM', style: TextStyle(
                  color: t.textTertiary, fontSize: 10, letterSpacing: 2)),
                Text('Library', style: TextStyle(
                  color: t.textPrimary, fontSize: 13, fontWeight: FontWeight.w600)),
              ],
            ),
          ),
          IconButton(
            icon: Icon(Icons.more_vert, color: t.textPrimary),
            onPressed: () {},
          ),
        ],
      ),
    );
  }

  Widget _buildAlbumArt(dynamic t, song) {
    return AnimatedContainer(
      duration: const Duration(milliseconds: 400),
      width: 280, height: 280,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16),
        gradient: LinearGradient(
          colors: [t.accent.withAlpha(100), t.accentSecondary.withAlpha(100)],
          begin: Alignment.topLeft, end: Alignment.bottomRight,
        ),
        boxShadow: [
          BoxShadow(
            color: t.accent.withAlpha(50), blurRadius: 40,
            offset: const Offset(0, 20),
          ),
        ],
      ),
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.music_note, size: 80, color: t.textPrimary.withAlpha(200)),
            const SizedBox(height: 8),
            Text(song?.album ?? '', textAlign: TextAlign.center,
              style: TextStyle(color: t.textPrimary.withAlpha(150), fontSize: 12),
              maxLines: 2, overflow: TextOverflow.ellipsis),
          ],
        ),
      ),
    );
  }

  Widget _buildSongInfo(song, dynamic t) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 32),
      child: Column(
        children: [
          Text(song?.title ?? 'No song', maxLines: 1, overflow: TextOverflow.ellipsis,
            textAlign: TextAlign.center,
            style: TextStyle(color: t.textPrimary, fontSize: 22, fontWeight: FontWeight.bold)),
          const SizedBox(height: 4),
          Text(song?.artist ?? 'Unknown', maxLines: 1, overflow: TextOverflow.ellipsis,
            textAlign: TextAlign.center,
            style: TextStyle(color: t.textSecondary, fontSize: 16)),
        ],
      ),
    );
  }

  Widget _buildProgressBar(PlayerProvider player, dynamic t) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 32),
      child: Column(
        children: [
          SliderTheme(
            data: SliderThemeData(
              activeTrackColor: t.accent,
              inactiveTrackColor: t.surfaceVariant,
              thumbColor: t.accent,
              thumbShape: const RoundSliderThumbShape(enabledThumbRadius: 6),
              trackHeight: 3,
              overlayShape: const RoundSliderOverlayShape(overlayRadius: 14),
            ),
            child: Slider(
              value: player.duration != null && player.duration!.inMilliseconds > 0
                  ? player.position.inMilliseconds.toDouble().clamp(
                      0, player.duration!.inMilliseconds.toDouble())
                  : 0,
              max: player.duration?.inMilliseconds.toDouble() ?? 1,
              onChanged: (v) => player.seek(Duration(milliseconds: v.toInt())),
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(_formatDuration(player.position), style: TextStyle(color: t.textTertiary, fontSize: 12)),
                Text(_formatDuration(player.duration ?? Duration.zero),
                    style: TextStyle(color: t.textTertiary, fontSize: 12)),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildControls(PlayerProvider player, dynamic t) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        IconButton(
          icon: Icon(Icons.skip_previous_rounded, color: t.textPrimary, size: 36),
          onPressed: player.hasPrevious ? () => player.skipPrevious() : null,
        ),
        const SizedBox(width: 16),
        Container(
          width: 72, height: 72,
          decoration: BoxDecoration(
            color: t.accent,
            shape: BoxShape.circle,
            boxShadow: [BoxShadow(color: t.accent.withAlpha(80), blurRadius: 20)],
          ),
          child: IconButton(
            icon: Icon(player.playing ? Icons.pause_rounded : Icons.play_arrow_rounded,
              color: t.background, size: 40),
            onPressed: () => player.togglePlay(),
          ),
        ),
        const SizedBox(width: 16),
        IconButton(
          icon: Icon(Icons.skip_next_rounded, color: t.textPrimary, size: 36),
          onPressed: player.hasNext ? () => player.skipNext() : null,
        ),
      ],
    );
  }

  Widget _buildSecondaryControls(PlayerProvider player, dynamic t) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        IconButton(
          icon: Icon(
            player.repeatMode == AppRepeatMode.one ? Icons.repeat_one :
            player.repeatMode == AppRepeatMode.all ? Icons.repeat : Icons.repeat,
            color: player.repeatMode != AppRepeatMode.off ? t.accent : t.textTertiary,
            size: 22,
          ),
          onPressed: () => player.toggleRepeat(),
        ),
        IconButton(
          icon: Icon(Icons.favorite_border, color: t.textTertiary, size: 22),
          onPressed: () {},
        ),
        IconButton(
          icon: Icon(Icons.shuffle, color: player.player.shuffleModeEnabled ? t.accent : t.textTertiary, size: 22),
          onPressed: () => player.toggleShuffle(),
        ),
        IconButton(
          icon: Icon(Icons.queue_music, color: t.textTertiary, size: 22),
          onPressed: () => _showQueue(context, player, t),
        ),
      ],
    );
  }

  void _showQueue(BuildContext context, PlayerProvider player, dynamic t) {
    showModalBottomSheet(
      context: context,
      backgroundColor: Colors.transparent,
      builder: (ctx) => Container(
        height: MediaQuery.of(ctx).size.height * 0.6,
        decoration: BoxDecoration(
          color: t.surface,
          borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
        ),
        child: Column(
          children: [
            Container(
              margin: const EdgeInsets.only(top: 12),
              width: 40, height: 4,
              decoration: BoxDecoration(color: t.textTertiary, borderRadius: BorderRadius.circular(2)),
            ),
            Padding(
              padding: const EdgeInsets.all(16),
              child: Text('Queue', style: TextStyle(color: t.textPrimary, fontSize: 18, fontWeight: FontWeight.bold)),
            ),
            Expanded(
              child: ListView.builder(
                itemCount: player.queue.length,
                itemBuilder: (ctx, i) {
                  final song = player.queue[i];
                  final isCurrent = i == player.currentIndex;
                  return ListTile(
                    leading: Icon(Icons.music_note,
                      color: isCurrent ? t.accent : t.textTertiary, size: 20),
                    title: Text(song.title, maxLines: 1, overflow: TextOverflow.ellipsis,
                      style: TextStyle(color: isCurrent ? t.accent : t.textPrimary, fontSize: 14)),
                    subtitle: Text(song.artist, maxLines: 1,
                      style: TextStyle(color: t.textTertiary, fontSize: 12)),
                    onTap: () {
                      player.skipToIndex(i);
                      Navigator.pop(ctx);
                    },
                  );
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  String _formatDuration(Duration d) {
    final min = d.inMinutes;
    final sec = d.inSeconds % 60;
    return '${min.toString().padLeft(2, '0')}:${sec.toString().padLeft(2, '0')}';
  }
}
