import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/player_provider.dart';
import '../../utils/colors.dart';

class QueueScreen extends StatelessWidget {
  const QueueScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final player = context.watch<PlayerProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Queue')),
      body: player.queue.isEmpty
          ? const Center(child: Text('Queue is empty', style: TextStyle(color: XolericColors.textTertiary)))
          : ReorderableListView.builder(
              padding: const EdgeInsets.only(bottom: 80),
              itemCount: player.queue.length,
              onReorder: (old, new_) => player.reorderQueue(old, new_),
              itemBuilder: (ctx, i) {
                final song = player.queue[i];
                final isCurrent = i == player.currentIndex;
                return ListTile(
                  key: ValueKey(song.id),
                  leading: Container(
                    width: 40, height: 40,
                    decoration: BoxDecoration(
                      color: isCurrent ? XolericColors.neonCyan.withValues(alpha: 0.2) : XolericColors.surfaceVariant,
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: isCurrent
                        ? const Icon(Icons.equalizer, color: XolericColors.neonCyan, size: 20)
                        : Center(child: Text('${i + 1}', style: const TextStyle(color: XolericColors.textTertiary))),
                  ),
                  title: Text(song.title, maxLines: 1, overflow: TextOverflow.ellipsis,
                      style: TextStyle(color: isCurrent ? XolericColors.neonCyan : XolericColors.textPrimary, fontSize: 14)),
                  subtitle: Text(song.artist, maxLines: 1, overflow: TextOverflow.ellipsis,
                      style: const TextStyle(color: XolericColors.textSecondary, fontSize: 12)),
                  trailing: Row(mainAxisSize: MainAxisSize.min, children: [
                    IconButton(
                      icon: const Icon(Icons.close, size: 18, color: XolericColors.textTertiary),
                      onPressed: () => player.removeFromQueue(i),
                    ),
                    const Icon(Icons.drag_handle, color: XolericColors.textTertiary),
                  ]),
                  onTap: () => player.skipToIndex(i),
                );
              },
            ),
    );
  }
}
