import 'dart:ui';

class XolericColors {
  XolericColors._();

  static const black = Color(0xFF0A0A0A);
  static const surface = Color(0xFF1E1E30);
  static const surfaceVariant = Color(0xFF2A2A40);
  static const surfaceElevated = Color(0xFF32324A);

  static const neonCyan = Color(0xFF00E5FF);
  static const neonBlue = Color(0xFF2979FF);
  static const neonViolet = Color(0xFF7C4DFF);
  static const neonMagenta = Color(0xFFE040FB);

  static const textPrimary = Color(0xFFFFFFFF);
  static const textSecondary = Color(0xB3FFFFFF);
  static const textTertiary = Color(0x66FFFFFF);

  static const divider = Color(0x1AFFFFFF);
  static const glass = Color(0x14FFFFFF);
  static const glassBorder = Color(0x1AFFFFFF);

  static const error = Color(0xFFFF5252);
  static const success = Color(0xFF69F0AE);

  static const miniPlayerBg = Color(0xE61A1A2E);
  static const bottomNavBg = Color(0xE60A0A0A);

  static Color fromAccentName(String name) {
    switch (name) {
      case 'blue': return neonBlue;
      case 'violet': return neonViolet;
      case 'magenta': return neonMagenta;
      default: return neonCyan;
    }
  }
}
