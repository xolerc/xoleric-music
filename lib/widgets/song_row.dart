import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/song.dart';
import '../providers/player_provider.dart';
import '../providers/settings_provider.dart';
import '../utils/colors.dart';

class SongRow extends StatelessWidget {
  final SongModel song;
  final List<SongModel>? queue;
  final int? index;
  final VoidCallback? onLongPress;

  const SongRow({super.key, required this.song, this.queue, this.index, this.onLongPress});

  @override
  Widget build(BuildContext context) {
    final player = context.watch<PlayerProvider>();
    final settings = context.watch<SettingsProvider>();
    final isCurrent = player.currentSong?.id == song.id;
    final isFav = settings.isFavorite(song.id);

    return ListTile(
      leading: Container(
        width: 44,
        height: 44,
        decoration: BoxDecoration(
          gradient: isCurrent
              ? LinearGradient(colors: [XolericColors.neonCyan.withValues(alpha: 0.3), XolericColors.neonBlue.withValues(alpha: 0.3)])
              : const LinearGradient(colors: [XolericColors.surfaceVariant, XolericColors.surfaceElevated]),
          borderRadius: BorderRadius.circular(8),
        ),
        child: isCurrent && player.playing
            ? const Icon(Icons.equalizer, color: XolericColors.neonCyan, size: 22)
            : const Icon(Icons.music_note, color: XolericColors.textTertiary, size: 22),
      ),
      title: Text(song.title, maxLines: 1, overflow: TextOverflow.ellipsis,
          style: TextStyle(color: isCurrent ? XolericColors.neonCyan : XolericColors.textPrimary, fontSize: 15, fontWeight: FontWeight.w500)),
      subtitle: Text(song.artist, maxLines: 1, overflow: TextOverflow.ellipsis,
          style: const TextStyle(color: XolericColors.textSecondary, fontSize: 13)),
      trailing: Row(mainAxisSize: MainAxisSize.min, children: [
        Text(song.durationText, style: const TextStyle(color: XolericColors.textTertiary, fontSize: 12)),
        const SizedBox(width: 4),
        GestureDetector(
          onTap: () => settings.toggleFavorite(song.id),
          child: Icon(isFav ? Icons.favorite : Icons.favorite_border,
              size: 18, color: isFav ? XolericColors.neonMagenta : XolericColors.textTertiary),
        ),
      ]),
      onTap: () => player.playSong(song, queue: queue ?? [song]),
      onLongPress: onLongPress,
    );
  }
}
