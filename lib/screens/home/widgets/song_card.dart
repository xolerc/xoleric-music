import 'package:flutter/material.dart';
import '../../../utils/colors.dart';
import '../../../widgets/glass_card.dart';

class SongCard extends StatelessWidget {
  final dynamic song;
  final VoidCallback? onTap;

  const SongCard({super.key, required this.song, this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: 140,
        margin: const EdgeInsets.only(right: 12),
        child: GlassCard(
          padding: const EdgeInsets.all(10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                height: 80,
                width: double.infinity,
                decoration: BoxDecoration(
                  gradient: LinearGradient(colors: [XolericColors.surfaceVariant, XolericColors.surface]),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: const Icon(Icons.music_note, size: 32, color: XolericColors.textTertiary),
              ),
              const SizedBox(height: 8),
              Text(song.title, style: const TextStyle(color: XolericColors.textPrimary, fontSize: 13, fontWeight: FontWeight.w500), maxLines: 1, overflow: TextOverflow.ellipsis),
              Text(song.artist, style: const TextStyle(color: XolericColors.textSecondary, fontSize: 11), maxLines: 1, overflow: TextOverflow.ellipsis),
            ],
          ),
        ),
      ),
    );
  }
}
