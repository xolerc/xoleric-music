import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../models/song.dart';
import '../../providers/music_provider.dart';
import '../../utils/colors.dart';
import '../../widgets/song_row.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen({super.key});
  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  final _controller = TextEditingController();

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final music = context.watch<MusicProvider>();
    final query = _controller.text.trim();
    final results = query.isEmpty ? <SongModel>[] : music.search(query);

    return Scaffold(
      appBar: AppBar(title: const Text('Search')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              controller: _controller,
              style: const TextStyle(color: XolericColors.textPrimary),
              decoration: InputDecoration(
                hintText: 'Search songs, artists...',
                hintStyle: const TextStyle(color: XolericColors.textTertiary),
                prefixIcon: const Icon(Icons.search, color: XolericColors.textTertiary),
                suffixIcon: query.isNotEmpty
                    ? IconButton(icon: const Icon(Icons.clear), onPressed: () { _controller.clear(); setState(() {}); })
                    : null,
                filled: true,
                fillColor: XolericColors.surface,
                border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
              ),
              onChanged: (_) => setState(() {}),
            ),
          ),
          Expanded(
            child: results.isEmpty
                ? Center(
                    child: Text(query.isEmpty ? 'Type to search...' : 'No results found',
                        style: const TextStyle(color: XolericColors.textTertiary)),
                  )
                : ListView.builder(
                    padding: const EdgeInsets.only(bottom: 80),
                    itemCount: results.length,
                    itemBuilder: (ctx, i) => SongRow(song: results[i], queue: results),
                  ),
          ),
        ],
      ),
    );
  }
}
