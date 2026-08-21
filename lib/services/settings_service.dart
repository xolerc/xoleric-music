import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';
import '../../utils/constants.dart';

class SettingsService {
  late SharedPreferences _prefs;

  Future<void> init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  String get accentColor => _prefs.getString(AppConstants.keyAccent) ?? AppConstants.defaultAccent;
  set accentColor(String value) => _prefs.setString(AppConstants.keyAccent, value);

  bool get autoPlay => _prefs.getBool(AppConstants.keyAutoPlay) ?? AppConstants.defaultAutoPlay;
  set autoPlay(bool value) => _prefs.setBool(AppConstants.keyAutoPlay, value);

  bool get showMiniPlayer => _prefs.getBool(AppConstants.keyMiniPlayer) ?? AppConstants.defaultShowMiniPlayer;
  set showMiniPlayer(bool value) => _prefs.setBool(AppConstants.keyMiniPlayer, value);

  bool get darkMode => _prefs.getBool(AppConstants.keyDarkMode) ?? AppConstants.defaultDarkMode;
  set darkMode(bool value) => _prefs.setBool(AppConstants.keyDarkMode, value);

  List<int> get favoriteIds {
    final raw = _prefs.getString('favorites');
    if (raw == null) return [];
    return (jsonDecode(raw) as List).cast<int>();
  }

  set favoriteIds(List<int> ids) {
    _prefs.setString('favorites', jsonEncode(ids));
  }

  void toggleFavorite(int songId) {
    final ids = favoriteIds;
    if (ids.contains(songId)) {
      ids.remove(songId);
    } else {
      ids.add(songId);
    }
    favoriteIds = ids;
  }

  bool isFavorite(int songId) => favoriteIds.contains(songId);
}
