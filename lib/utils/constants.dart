class AppConstants {
  static const String appName = 'XOLERIC';
  static const String version = 'v1.0.0';

  static const List<String> accentOptions = ['cyan', 'blue', 'violet', 'magenta'];
  static const String defaultAccent = 'cyan';

  static const bool defaultShuffle = false;
  static const bool defaultAutoPlay = true;
  static const bool defaultShowMiniPlayer = true;
  static const bool defaultDarkMode = true;

  static const String keyAccent = 'accent_color';
  static const String keyAutoPlay = 'auto_play';
  static const String keyMiniPlayer = 'show_mini_player';
  static const String keyDarkMode = 'dark_mode';

  static const String playlistsFile = 'playlists.json';
}

enum AppRepeatMode { off, all, one }
