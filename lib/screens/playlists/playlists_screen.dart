import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';
import '../../providers/playlist_provider.dart';
import '../../utils/colors.dart';
import '../../widgets/empty_state.dart';

class PlaylistsScreen extends StatelessWidget {
  const PlaylistsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final playlists = context.watch<PlaylistProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Playlists')),
      floatingActionButton: FloatingActionButton(
        backgroundColor: XolericColors.neonCyan,
        foregroundColor: XolericColors.black,
        onPressed: () => _showCreateDialog(context),
        child: const Icon(Icons.add),
      ),
      body: playlists.playlists.isEmpty
          ? const EmptyState(icon: Icons.queue_music, message: 'No playlists yet.\nTap + to create one.')
          : ListView.builder(
              padding: const EdgeInsets.only(bottom: 80),
              itemCount: playlists.playlists.length,
              itemBuilder: (ctx, i) {
                final pl = playlists.playlists[i];
                return ListTile(
                  leading: Container(
                    width: 48, height: 48,
                    decoration: BoxDecoration(
                      gradient: LinearGradient(colors: [XolericColors.neonCyan.withValues(alpha: 0.3), XolericColors.neonBlue.withValues(alpha: 0.3)]),
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: const Icon(Icons.queue_music, color: XolericColors.neonCyan),
                  ),
                  title: Text(pl.name, style: const TextStyle(color: XolericColors.textPrimary, fontSize: 16)),
                  subtitle: Text('${pl.songIds.length} songs', style: const TextStyle(color: XolericColors.textSecondary, fontSize: 13)),
                  trailing: PopupMenuButton(
                    icon: const Icon(Icons.more_vert, color: XolericColors.textTertiary),
                    itemBuilder: (_) => [
                      const PopupMenuItem(value: 'rename', child: Text('Rename')),
                      const PopupMenuItem(value: 'delete', child: Text('Delete', style: TextStyle(color: XolericColors.error))),
                    ],
                    onSelected: (v) {
                      if (v == 'delete') playlists.delete(pl.id);
                      if (v == 'rename') _showRenameDialog(context, pl.id, pl.name);
                    },
                  ),
                  onTap: () => context.push('/playlist/${pl.id}'),
                );
              },
            ),
    );
  }

  void _showCreateDialog(BuildContext context) {
    final ctrl = TextEditingController();
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        backgroundColor: XolericColors.surface,
        title: const Text('New Playlist', style: TextStyle(color: XolericColors.textPrimary)),
        content: TextField(
          controller: ctrl,
          autofocus: true,
          style: const TextStyle(color: XolericColors.textPrimary),
          decoration: const InputDecoration(hintText: 'Playlist name'),
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')),
          FilledButton(
            onPressed: () {
              if (ctrl.text.trim().isNotEmpty) {
                context.read<PlaylistProvider>().create(ctrl.text.trim());
                Navigator.pop(context);
              }
            },
            style: FilledButton.styleFrom(backgroundColor: XolericColors.neonCyan, foregroundColor: XolericColors.black),
            child: const Text('Create'),
          ),
        ],
      ),
    );
  }

  void _showRenameDialog(BuildContext context, String id, String currentName) {
    final ctrl = TextEditingController(text: currentName);
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        backgroundColor: XolericColors.surface,
        title: const Text('Rename Playlist', style: TextStyle(color: XolericColors.textPrimary)),
        content: TextField(controller: ctrl, autofocus: true, style: const TextStyle(color: XolericColors.textPrimary)),
        actions: [
          TextButton(onPressed: () => Navigator.pop(context), child: const Text('Cancel')),
          FilledButton(
            onPressed: () {
              if (ctrl.text.trim().isNotEmpty) {
                context.read<PlaylistProvider>().rename(id, ctrl.text.trim());
                Navigator.pop(context);
              }
            },
            style: FilledButton.styleFrom(backgroundColor: XolericColors.neonCyan, foregroundColor: XolericColors.black),
            child: const Text('Save'),
          ),
        ],
      ),
    );
  }
}
