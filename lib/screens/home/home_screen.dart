import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../models/song.dart';
import '../../providers/music_provider.dart';
import '../../providers/player_provider.dart';
import '../../providers/theme_provider.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});
  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  String _searchQuery = '';
  bool _showSearch = false;

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
    final t = context.watch<ThemeProvider>().current;

    final songs = _searchQuery.isEmpty
        ? music.songs
        : music.songs.where((s) =>
            s.title.toLowerCase().contains(_searchQuery.toLowerCase()) ||
            s.artist.toLowerCase().contains(_searchQuery.toLowerCase())).toList();

    return Scaffold(
      backgroundColor: t.background,
      body: SafeArea(
        child: Column(
          children: [
            _buildHeader(t),
            if (_showSearch) _buildSearchBar(t),
            Expanded(
              child: music.loading
                  ? Center(child: CircularProgressIndicator(color: t.accent))
                  : songs.isEmpty
                      ? _buildEmpty(t)
                      : _buildSongList(songs, player, music, t),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildHeader(dynamic t) {
    return Container(
      padding: const EdgeInsets.fromLTRB(20, 16, 20, 8),
      child: Row(
        children: [
          Container(
            padding: const EdgeInsets.all(8),
            decoration: BoxDecoration(
              color: t.accent.withAlpha(30),
              borderRadius: BorderRadius.circular(t.buttonRadius),
            ),
            child: Icon(Icons.album, color: t.accent, size: 24),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Text('XOLERIC', style: TextStyle(
              color: t.textPrimary, fontSize: 22, fontWeight: FontWeight.bold, letterSpacing: 2,
            )),
          ),
          IconButton(
            icon: Icon(_showSearch ? Icons.close : Icons.search, color: t.textPrimary),
            onPressed: () => setState(() {
              _showSearch = !_showSearch;
              if (!_showSearch) _searchQuery = '';
            }),
          ),
        ],
      ),
    );
  }

  Widget _buildSearchBar(dynamic t) {
    return Container(
      margin: const EdgeInsets.symmetric(horizontal: 20, vertical: 8),
      decoration: BoxDecoration(
        color: t.surface,
        borderRadius: BorderRadius.circular(t.buttonRadius),
      ),
      child: TextField(
        autofocus: true,
        style: TextStyle(color: t.textPrimary),
        decoration: InputDecoration(
          hintText: 'Search songs, artists...',
          hintStyle: TextStyle(color: t.textTertiary),
          prefixIcon: Icon(Icons.search, color: t.textTertiary),
          border: InputBorder.none,
          contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        ),
        onChanged: (v) => setState(() => _searchQuery = v),
      ),
    );
  }

  Widget _buildEmpty(dynamic t) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(Icons.music_note, size: 64, color: t.textTertiary.withAlpha(100)),
          const SizedBox(height: 16),
          Text('No music found', style: TextStyle(color: t.textSecondary, fontSize: 16)),
          const SizedBox(height: 8),
          Text('Grant storage permission and restart', style: TextStyle(color: t.textTertiary, fontSize: 13)),
          const SizedBox(height: 24),
          ElevatedButton.icon(
            onPressed: () => context.read<MusicProvider>().loadSongs(),
            icon: const Icon(Icons.refresh, size: 18),
            label: const Text('Retry'),
            style: ElevatedButton.styleFrom(
              backgroundColor: t.accent, foregroundColor: t.background,
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(t.buttonRadius)),
              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSongList(List<SongModel> songs, PlayerProvider player, MusicProvider music, dynamic t) {
    return ListView.builder(
      padding: const EdgeInsets.only(bottom: 100),
      itemCount: songs.length,
      itemBuilder: (ctx, i) {
        final song = songs[i];
        final isPlaying = player.currentSong?.id == song.id && player.playing;

        return Container(
          margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
          decoration: BoxDecoration(
            color: isPlaying ? t.accent.withAlpha(20) : Colors.transparent,
            borderRadius: BorderRadius.circular(t.cardRadius),
          ),
          child: ListTile(
            contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 2),
            leading: Container(
              width: 48, height: 48,
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  colors: [t.accent.withAlpha(80), t.accentSecondary.withAlpha(80)],
                  begin: Alignment.topLeft, end: Alignment.bottomRight,
                ),
                borderRadius: BorderRadius.circular(12),
              ),
              child: Center(
                child: isPlaying
                    ? Icon(Icons.equalizer, color: t.accent, size: 24)
                    : Icon(Icons.music_note, color: t.textPrimary, size: 24),
              ),
            ),
            title: Text(song.title, maxLines: 1, overflow: TextOverflow.ellipsis,
              style: TextStyle(color: t.textPrimary, fontSize: 15,
                fontWeight: isPlaying ? FontWeight.w600 : FontWeight.normal)),
            subtitle: Text(song.artist, maxLines: 1, overflow: TextOverflow.ellipsis,
              style: TextStyle(color: t.textTertiary, fontSize: 13)),
            trailing: Text(song.durationText, style: TextStyle(color: t.textTertiary, fontSize: 12)),
            onTap: () => player.playSong(song, queueList: music.songs),
          ),
        );
      },
    );
  }
}
