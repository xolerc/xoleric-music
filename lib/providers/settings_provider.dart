import 'package:flutter/material.dart';
import '../../services/settings_service.dart';

class SettingsProvider extends ChangeNotifier {
  final SettingsService _service = SettingsService();
  bool _loaded = false;

  bool get loaded => _loaded;
  String get accentColor => _service.accentColor;
  bool get autoPlay => _service.autoPlay;
  bool get showMiniPlayer => _service.showMiniPlayer;
  bool get darkMode => _service.darkMode;

  Future<void> init() async {
    await _service.init();
    _loaded = true;
    notifyListeners();
  }

  set accentColor(String value) {
    _service.accentColor = value;
    notifyListeners();
  }

  set autoPlay(bool value) {
    _service.autoPlay = value;
    notifyListeners();
  }

  set showMiniPlayer(bool value) {
    _service.showMiniPlayer = value;
    notifyListeners();
  }

  set darkMode(bool value) {
    _service.darkMode = value;
    notifyListeners();
  }

  void toggleFavorite(int songId) {
    _service.toggleFavorite(songId);
    notifyListeners();
  }

  bool isFavorite(int songId) => _service.isFavorite(songId);
}
