import 'package:on_audio_query/on_audio_query.dart' as aq;
import '../../models/song.dart';

class MusicLibraryService {
  final aq.OnAudioQuery _audioQuery = aq.OnAudioQuery();
  List<SongModel> _songs = [];
  bool _scanned = false;

  List<SongModel> get songs => _songs;
  bool get scanned => _scanned;

  Future<void> scanSongs() async {
    final rawSongs = await _audioQuery.querySongs(
      sortType: aq.SongSortType.DATE_ADDED,
      orderType: aq.OrderType.DESC_OR_GREATER,
      uriType: aq.UriType.EXTERNAL,
    );
    _songs = rawSongs
        .where((s) => s.duration != null && s.duration! > 0)
        .map((s) => SongModel(
              id: s.id,
              title: s.title,
              artist: s.artist ?? 'Unknown Artist',
              album: s.album ?? 'Unknown Album',
              duration: s.duration!,
              path: s.data,
              size: s.size,
              dateAdded: s.dateAdded ?? 0,
              artworkId: s.albumId,
            ))
        .toList();
    _scanned = true;
  }

  List<SongModel> get favorites => _songs.where((s) => false).toList(); // TODO: integrate with favorites

  List<SongModel> search(String query) {
    final q = query.toLowerCase();
    return _songs.where((s) =>
        s.title.toLowerCase().contains(q) ||
        s.artist.toLowerCase().contains(q) ||
        s.album.toLowerCase().contains(q)).toList();
  }

  List<String> get genres => _songs.map((s) => s.album).toSet().toList();
}
