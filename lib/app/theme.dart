import 'package:flutter/material.dart';
import '../utils/colors.dart';

class XolericTheme {
  XolericTheme._();

  static ThemeData dark() {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.dark,
      scaffoldBackgroundColor: XolericColors.black,
      colorScheme: ColorScheme.dark(
        primary: XolericColors.neonCyan,
        onPrimary: XolericColors.black,
        secondary: XolericColors.neonBlue,
        surface: XolericColors.surface,
        onSurface: XolericColors.textPrimary,
        error: XolericColors.error,
      ),
      appBarTheme: const AppBarTheme(
        backgroundColor: Colors.transparent,
        elevation: 0,
        centerTitle: false,
        titleTextStyle: TextStyle(
          color: XolericColors.textPrimary,
          fontSize: 20,
          fontWeight: FontWeight.w600,
        ),
        iconTheme: IconThemeData(color: XolericColors.textPrimary),
      ),
      cardTheme: CardThemeData(
        color: XolericColors.surface,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        elevation: 0,
      ),
      navigationBarTheme: NavigationBarThemeData(
        backgroundColor: XolericColors.bottomNavBg,
        indicatorColor: XolericColors.neonCyan.withValues(alpha: 0.15),
        elevation: 0,
        labelTextStyle: WidgetStateProperty.resolveWith((states) {
          if (states.contains(WidgetState.selected)) {
            return const TextStyle(color: XolericColors.neonCyan, fontSize: 12, fontWeight: FontWeight.w500);
          }
          return const TextStyle(color: XolericColors.textTertiary, fontSize: 12);
        }),
        iconTheme: WidgetStateProperty.resolveWith((states) {
          if (states.contains(WidgetState.selected)) {
            return const IconThemeData(color: XolericColors.neonCyan, size: 24);
          }
          return const IconThemeData(color: XolericColors.textTertiary, size: 24);
        }),
      ),
      sliderTheme: const SliderThemeData(
        activeTrackColor: XolericColors.neonCyan,
        inactiveTrackColor: XolericColors.surfaceElevated,
        thumbColor: XolericColors.neonCyan,
        thumbShape: RoundSliderThumbShape(enabledThumbRadius: 6),
        trackHeight: 3,
      ),
      iconTheme: const IconThemeData(color: XolericColors.textPrimary),
      textTheme: const TextTheme(
        headlineLarge: TextStyle(color: XolericColors.textPrimary, fontWeight: FontWeight.bold),
        headlineMedium: TextStyle(color: XolericColors.textPrimary, fontWeight: FontWeight.w600),
        titleLarge: TextStyle(color: XolericColors.textPrimary, fontWeight: FontWeight.w600),
        titleMedium: TextStyle(color: XolericColors.textPrimary),
        bodyLarge: TextStyle(color: XolericColors.textPrimary),
        bodyMedium: TextStyle(color: XolericColors.textSecondary),
        bodySmall: TextStyle(color: XolericColors.textTertiary),
        labelSmall: TextStyle(color: XolericColors.textTertiary),
      ),
    );
  }
}
