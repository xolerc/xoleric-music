import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../../models/playlist.dart';
import '../../utils/constants.dart';

class PlaylistService {
  List<PlaylistModel> _playlists = [];
  List<PlaylistModel> get playlists => _playlists;

  Future<void> load() async {
    final prefs = await SharedPreferences.getInstance();
    final raw = prefs.getString(AppConstants.playlistsFile);
    if (raw != null) {
      final list = (jsonDecode(raw) as List).cast<Map<String, dynamic>>();
      _playlists = list.map((j) => PlaylistModel.fromJson(j)).toList();
    }
  }

  Future<void> save() async {
    final prefs = await SharedPreferences.getInstance();
    final json = _playlists.map((p) => p.toJson()).toList();
    await prefs.setString(AppConstants.playlistsFile, jsonEncode(json));
  }

  Future<void> create(String name) async {
    final playlist = PlaylistModel(
      id: DateTime.now().millisecondsSinceEpoch.toString(),
      name: name,
      songIds: [],
      createdAt: DateTime.now(),
    );
    _playlists.add(playlist);
    await save();
  }

  Future<void> delete(String id) async {
    _playlists.removeWhere((p) => p.id == id);
    await save();
  }

  Future<void> rename(String id, String newName) async {
    final idx = _playlists.indexWhere((p) => p.id == id);
    if (idx >= 0) {
      _playlists[idx] = _playlists[idx].copyWith(name: newName);
      await save();
    }
  }

  Future<void> addSong(String playlistId, int songId) async {
    final idx = _playlists.indexWhere((p) => p.id == playlistId);
    if (idx >= 0 && !_playlists[idx].songIds.contains(songId)) {
      final newIds = List<int>.from(_playlists[idx].songIds)..add(songId);
      _playlists[idx] = _playlists[idx].copyWith(songIds: newIds);
      await save();
    }
  }

  Future<void> removeSong(String playlistId, int songId) async {
    final idx = _playlists.indexWhere((p) => p.id == playlistId);
    if (idx >= 0) {
      final newIds = List<int>.from(_playlists[idx].songIds)..remove(songId);
      _playlists[idx] = _playlists[idx].copyWith(songIds: newIds);
      await save();
    }
  }
}
