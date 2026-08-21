import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../app/theme_styles.dart';

class ThemeProvider extends ChangeNotifier {
  XolericThemeStyle _current = XolericThemeStyle.ios;
  XolericThemeStyle get current => _current;

  ThemeProvider() {
    _load();
  }

  Future<void> _load() async {
    final prefs = await SharedPreferences.getInstance();
    final name = prefs.getString('theme_style') ?? 'iOS';
    _current = XolericThemeStyle.fromName(name);
    notifyListeners();
  }

  Future<void> setTheme(XolericThemeStyle style) async {
    _current = style;
    notifyListeners();
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('theme_style', style.name);
  }
}
