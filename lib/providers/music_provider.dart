import 'package:flutter/material.dart';
import '../models/song.dart';
import '../services/music_library_service.dart';

class MusicProvider extends ChangeNotifier {
  final MusicLibraryService _service = MusicLibraryService();
  List<SongModel> _songs = [];
  bool _loading = false;
  String _error = '';

  List<SongModel> get songs => _songs;
  bool get loading => _loading;
  String get error => _error;
  int get songCount => _songs.length;

  Future<void> loadSongs() async {
    _loading = true;
    _error = '';
    notifyListeners();

    try {
      await _service.scanSongs();
      _songs = _service.songs;
      _error = '';
    } catch (e) {
      debugPrint('MusicProvider error: $e');
      _error = 'Failed to load music.\nCheck permissions and try again.';
      _songs = [];
    }

    _loading = false;
    notifyListeners();
  }

  List<SongModel> search(String query) {
    try {
      return _service.search(query);
    } catch (e) {
      return [];
    }
  }
}
