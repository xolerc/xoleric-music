import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/theme_provider.dart';
import '../screens/home/home_screen.dart';
import '../screens/downloads/downloads_screen.dart';
import '../screens/settings/settings_screen.dart';
import '../widgets/mini_player.dart';

class XolericApp extends StatelessWidget {
  const XolericApp({super.key});

  @override
  Widget build(BuildContext context) {
    final themeProvider = context.watch<ThemeProvider>();
    final t = themeProvider.current;

    return MaterialApp(
      title: 'XOLERIC',
      debugShowCheckedModeBanner: false,
      theme: t.toThemeData(),
      home: const MainShell(),
    );
  }
}

class MainShell extends StatefulWidget {
  const MainShell({super.key});
  @override
  State<MainShell> createState() => _MainShellState();
}

class _MainShellState extends State<MainShell> {
  int _index = 0;
  final _pages = const [
    HomeScreen(),
    DownloadsScreen(),
    SettingsScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    final t = context.read<ThemeProvider>().current;

    return Scaffold(
      body: IndexedStack(index: _index, children: _pages),
      bottomNavigationBar: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          const MiniPlayer(),
          Container(
            decoration: BoxDecoration(
              color: t.surface.withAlpha(230),
              border: Border(top: BorderSide(color: t.divider, width: 0.5)),
            ),
            child: SafeArea(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    _navItem(0, Icons.home_rounded, 'Home', t),
                    _navItem(1, Icons.download_rounded, 'Downloads', t),
                    _navItem(2, Icons.settings_rounded, 'Settings', t),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _navItem(int i, IconData icon, String label, dynamic t) {
    final active = _index == i;
    return GestureDetector(
      onTap: () => setState(() => _index = i),
      behavior: HitTestBehavior.opaque,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        decoration: BoxDecoration(
          color: active ? t.accent.withAlpha(30) : Colors.transparent,
          borderRadius: BorderRadius.circular(t.buttonRadius),
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(icon, color: active ? t.accent : t.textTertiary, size: 24),
            const SizedBox(height: 2),
            Text(label, style: TextStyle(
              color: active ? t.accent : t.textTertiary,
              fontSize: 11, fontWeight: active ? FontWeight.w600 : FontWeight.normal,
            )),
          ],
        ),
      ),
    );
  }
}
