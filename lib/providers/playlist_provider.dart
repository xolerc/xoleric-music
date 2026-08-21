import 'package:flutter/material.dart';
import '../../models/playlist.dart';
import '../../services/playlist_service.dart';

class PlaylistProvider extends ChangeNotifier {
  final PlaylistService _service = PlaylistService();
  bool _loaded = false;

  List<PlaylistModel> get playlists => _service.playlists;
  bool get loaded => _loaded;

  Future<void> load() async {
    await _service.load();
    _loaded = true;
    notifyListeners();
  }

  PlaylistModel? getById(String id) {
    try {
      return _service.playlists.firstWhere((p) => p.id == id);
    } catch (_) {
      return null;
    }
  }

  Future<void> create(String name) async {
    await _service.create(name);
    notifyListeners();
  }

  Future<void> delete(String id) async {
    await _service.delete(id);
    notifyListeners();
  }

  Future<void> rename(String id, String newName) async {
    await _service.rename(id, newName);
    notifyListeners();
  }

  Future<void> addSong(String playlistId, int songId) async {
    await _service.addSong(playlistId, songId);
    notifyListeners();
  }

  Future<void> removeSong(String playlistId, int songId) async {
    await _service.removeSong(playlistId, songId);
    notifyListeners();
  }
}
