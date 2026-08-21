import 'package:flutter/material.dart';

class XolericThemeStyle {
  final String name;
  final String icon;
  final Color accent;
  final Color accentSecondary;
  final Color surface;
  final Color surfaceVariant;
  final Color background;
  final Color card;
  final Color textPrimary;
  final Color textSecondary;
  final Color textTertiary;
  final Color divider;
  final double cardRadius;
  final double buttonRadius;
  final bool useGlass;

  const XolericThemeStyle({
    required this.name,
    required this.icon,
    required this.accent,
    required this.accentSecondary,
    required this.surface,
    required this.surfaceVariant,
    required this.background,
    required this.card,
    required this.textPrimary,
    required this.textSecondary,
    required this.textTertiary,
    required this.divider,
    required this.cardRadius,
    required this.buttonRadius,
    required this.useGlass,
  });

  static const ios = XolericThemeStyle(
    name: 'iOS', icon: '🍎',
    accent: Color(0xFF007AFF), accentSecondary: Color(0xFF5856D6),
    surface: Color(0xFF1C1C1E), surfaceVariant: Color(0xFF2C2C2E),
    background: Color(0xFF000000), card: Color(0xFF1C1C1E),
    textPrimary: Colors.white, textSecondary: Color(0xFFE5E5EA),
    textTertiary: Color(0xFF636366), divider: Color(0xFF38383A),
    cardRadius: 16, buttonRadius: 12, useGlass: true,
  );

  static const materialYou = XolericThemeStyle(
    name: 'Material You', icon: '🎨',
    accent: Color(0xFFBB86FC), accentSecondary: Color(0xFF03DAC6),
    surface: Color(0xFF1E1E2E), surfaceVariant: Color(0xFF2D2D3F),
    background: Color(0xFF121218), card: Color(0xFF1E1E2E),
    textPrimary: Color(0xFFE6E1E5), textSecondary: Color(0xFFCAC4D0),
    textTertiary: Color(0xFF938F99), divider: Color(0xFF49454F),
    cardRadius: 28, buttonRadius: 20, useGlass: false,
  );

  static const oneUi = XolericThemeStyle(
    name: 'One UI', icon: '📱',
    accent: Color(0xFF2196F3), accentSecondary: Color(0xFF00BCD4),
    surface: Color(0xFF1A1A2E), surfaceVariant: Color(0xFF252540),
    background: Color(0xFF0D0D1A), card: Color(0xFF1A1A2E),
    textPrimary: Colors.white, textSecondary: Color(0xFFB0B0C0),
    textTertiary: Color(0xFF606080), divider: Color(0xFF2A2A40),
    cardRadius: 24, buttonRadius: 16, useGlass: true,
  );

  static const miui = XolericThemeStyle(
    name: 'MIUI', icon: '🔶',
    accent: Color(0xFFFF6B35), accentSecondary: Color(0xFFFFB74D),
    surface: Color(0xFF1E1E30), surfaceVariant: Color(0xFF2A2A42),
    background: Color(0xFF0A0A18), card: Color(0xFF1E1E30),
    textPrimary: Colors.white, textSecondary: Color(0xFFB0B0C8),
    textTertiary: Color(0xFF606088), divider: Color(0xFF2A2A42),
    cardRadius: 24, buttonRadius: 14, useGlass: false,
  );

  static const nothing = XolericThemeStyle(
    name: 'Nothing OS', icon: '⚫',
    accent: Color(0xFFE0E0E0), accentSecondary: Color(0xFFFF6B35),
    surface: Color(0xFF1A1A1A), surfaceVariant: Color(0xFF2A2A2A),
    background: Color(0xFF000000), card: Color(0xFF1A1A1A),
    textPrimary: Color(0xFFE0E0E0), textSecondary: Color(0xFF9E9E9E),
    textTertiary: Color(0xFF616161), divider: Color(0xFF333333),
    cardRadius: 20, buttonRadius: 50, useGlass: false,
  );

  static List<XolericThemeStyle> get all => [ios, materialYou, oneUi, miui, nothing];

  static XolericThemeStyle fromName(String name) =>
      all.firstWhere((t) => t.name == name, orElse: () => ios);

  ThemeData toThemeData() {
    return ThemeData(
      useMaterial3: true, brightness: Brightness.dark,
      scaffoldBackgroundColor: background,
      colorScheme: ColorScheme.dark(
        primary: accent, secondary: accentSecondary, surface: surface,
        onSurface: textPrimary, error: const Color(0xFFFF5252),
      ),
      appBarTheme: AppBarTheme(
        backgroundColor: Colors.transparent, elevation: 0,
        centerTitle: true,
        titleTextStyle: TextStyle(color: textPrimary, fontSize: 18, fontWeight: FontWeight.w600),
        iconTheme: IconThemeData(color: textPrimary),
      ),
      cardTheme: CardThemeData(
        color: card, elevation: 0,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(cardRadius)),
      ),
      sliderTheme: SliderThemeData(
        activeTrackColor: accent, inactiveTrackColor: surfaceVariant,
        thumbColor: accent, thumbShape: const RoundSliderThumbShape(enabledThumbRadius: 6),
        trackHeight: 3,
      ),
      iconTheme: IconThemeData(color: textPrimary),
    );
  }
}
