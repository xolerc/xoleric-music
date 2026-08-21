import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/theme_provider.dart';
import '../../app/theme_styles.dart';

class SettingsScreen extends StatelessWidget {
  const SettingsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final themeProvider = context.watch<ThemeProvider>();
    final t = themeProvider.current;

    return Scaffold(
      backgroundColor: t.background,
      body: SafeArea(
        child: ListView(
          padding: const EdgeInsets.all(20),
          children: [
            Text('Settings', style: TextStyle(
              color: t.textPrimary, fontSize: 28, fontWeight: FontWeight.bold)),
            const SizedBox(height: 24),
            _sectionTitle('Theme', t),
            const SizedBox(height: 12),
            _themeGrid(context, themeProvider, t),
            const SizedBox(height: 24),
            _sectionTitle('About', t),
            const SizedBox(height: 12),
            _aboutCard(t),
            const SizedBox(height: 100),
          ],
        ),
      ),
    );
  }

  Widget _sectionTitle(String text, dynamic t) {
    return Text(text.toUpperCase(), style: TextStyle(
      color: t.textTertiary, fontSize: 12, letterSpacing: 2, fontWeight: FontWeight.w600));
  }

  Widget _themeGrid(BuildContext context, ThemeProvider provider, dynamic t) {
    return GridView.builder(
      shrinkWrap: true,
      physics: const NeverScrollableScrollPhysics(),
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2, childAspectRatio: 1.2, crossAxisSpacing: 12, mainAxisSpacing: 12),
      itemCount: XolericThemeStyle.all.length,
      itemBuilder: (ctx, i) {
        final style = XolericThemeStyle.all[i];
        final selected = provider.current.name == style.name;

        return GestureDetector(
          onTap: () => provider.setTheme(style),
          child: AnimatedContainer(
            duration: const Duration(milliseconds: 200),
            decoration: BoxDecoration(
              color: selected ? style.accent.withAlpha(30) : t.surface,
              borderRadius: BorderRadius.circular(t.cardRadius),
              border: Border.all(
                color: selected ? style.accent : t.divider,
                width: selected ? 2 : 1,
              ),
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(style.icon, style: const TextStyle(fontSize: 32)),
                const SizedBox(height: 8),
                Text(style.name, style: TextStyle(
                  color: selected ? style.accent : t.textPrimary,
                  fontSize: 14, fontWeight: FontWeight.w600,
                )),
                const SizedBox(height: 4),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    _dot(style.accent),
                    const SizedBox(width: 4),
                    _dot(style.surface),
                    const SizedBox(width: 4),
                    _dot(style.surfaceVariant),
                  ],
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget _dot(Color c) {
    return Container(width: 10, height: 10,
      decoration: BoxDecoration(color: c, shape: BoxShape.circle));
  }

  Widget _aboutCard(dynamic t) {
    return Container(
      padding: const EdgeInsets.all(20),
      decoration: BoxDecoration(
        color: t.surface, borderRadius: BorderRadius.circular(t.cardRadius)),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(
                  color: t.accent.withAlpha(30),
                  borderRadius: BorderRadius.circular(t.buttonRadius)),
                child: Icon(Icons.album, color: t.accent, size: 24),
              ),
              const SizedBox(width: 12),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('XOLERIC', style: TextStyle(color: t.textPrimary, fontSize: 18, fontWeight: FontWeight.bold)),
                  Text('v1.1.0 Flutter', style: TextStyle(color: t.textTertiary, fontSize: 12)),
                ],
              ),
            ],
          ),
          const SizedBox(height: 16),
          Text('Professional Music Player & Download Manager',
            style: TextStyle(color: t.textSecondary, fontSize: 14)),
          const SizedBox(height: 8),
          Text('Built with Flutter • 5 Premium Themes',
            style: TextStyle(color: t.textTertiary, fontSize: 12)),
        ],
      ),
    );
  }
}
