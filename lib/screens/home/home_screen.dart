import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';
import '../../providers/music_provider.dart';
import '../../providers/player_provider.dart';
import '../../utils/colors.dart';
import '../../widgets/section_header.dart';
import '../../widgets/loading_indicator.dart';
import '../../widgets/empty_state.dart';
import 'widgets/song_card.dart';
import 'widgets/library_quick_item.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});
  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<MusicProvider>().loadSongs();
    });
  }

  @override
  Widget build(BuildContext context) {
    final music = context.watch<MusicProvider>();
    final player = context.watch<PlayerProvider>();

    return Scaffold(
      appBar: AppBar(
        title: const Row(children: [
          Icon(Icons.album, color: XolericColors.neonCyan, size: 28),
          SizedBox(width: 8),
          Text('XOLERIC', style: TextStyle(color: XolericColors.textPrimary, fontWeight: FontWeight.bold, letterSpacing: 2)),
        ]),
        actions: [
          IconButton(icon: const Icon(Icons.download_outlined), onPressed: () => context.push('/downloads')),
        ],
      ),
      body: music.loading
          ? const LoadingIndicator()
          : music.error.isNotEmpty
              ? EmptyState(icon: Icons.error_outline, message: music.error, actionLabel: 'Retry', onAction: () => music.loadSongs())
              : music.songs.isEmpty
                  ? const EmptyState(icon: Icons.music_note, message: 'No music found.\nTap the scan button to find music.')
                  : RefreshIndicator(
                      onRefresh: () => music.loadSongs(),
                      color: XolericColors.neonCyan,
                      child: ListView(
                        padding: const EdgeInsets.only(bottom: 100),
                        children: [
                          SectionHeader(title: 'Quick Library',
                              actionText: 'See all', onAction: () => context.go('/library')),
                          Padding(
                            padding: const EdgeInsets.symmetric(horizontal: 16),
                            child: Row(
                              children: [
                                LibraryQuickItem(icon: Icons.album, label: 'Albums', onTap: () => context.push('/albums')),
                                const SizedBox(width: 12),
                                LibraryQuickItem(icon: Icons.person, label: 'Artists', onTap: () => context.push('/artists')),
                                const SizedBox(width: 12),
                                LibraryQuickItem(icon: Icons.queue_music, label: 'Playlists', onTap: () => context.push('/playlists')),
                              ],
                            ),
                          ),
                          const SizedBox(height: 8),
                          SectionHeader(title: 'Recently Played',
                              actionText: 'See all', onAction: () => context.go('/library')),
                          SizedBox(
                            height: 180,
                            child: ListView.builder(
                              scrollDirection: Axis.horizontal,
                              padding: const EdgeInsets.symmetric(horizontal: 16),
                              itemCount: music.songs.take(10).length,
                              itemBuilder: (ctx, i) {
                                final song = music.songs[i];
                                return SongCard(
                                  song: song,
                                  onTap: () => player.playSong(song, queue: music.songs),
                                );
                              },
                            ),
                          ),
                          const SizedBox(height: 8),
                          SectionHeader(title: 'All Songs'),
                          ListView.builder(
                            shrinkWrap: true,
                            physics: const NeverScrollableScrollPhysics(),
                            padding: const EdgeInsets.only(bottom: 80),
                            itemCount: music.songs.length,
                            itemBuilder: (ctx, i) {
                              final song = music.songs[i];
                              return ListTile(
                                leading: Container(
                                  width: 44, height: 44,
                                  decoration: BoxDecoration(
                                    color: XolericColors.surfaceVariant,
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                  child: const Icon(Icons.music_note, color: XolericColors.textTertiary, size: 22),
                                ),
                                title: Text(song.title, maxLines: 1, overflow: TextOverflow.ellipsis,
                                    style: const TextStyle(color: XolericColors.textPrimary, fontSize: 15)),
                                subtitle: Text(song.artist, maxLines: 1, overflow: TextOverflow.ellipsis,
                                    style: const TextStyle(color: XolericColors.textSecondary, fontSize: 13)),
                                trailing: Text(song.durationText, style: const TextStyle(color: XolericColors.textTertiary, fontSize: 12)),
                                onTap: () => player.playSong(song, queue: music.songs),
                              );
                            },
                          ),
                        ],
                      ),
                    ),
    );
  }
}
