import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/settings_provider.dart';
import '../../utils/colors.dart';
import '../../utils/constants.dart';

class SettingsScreen extends StatelessWidget {
  const SettingsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final settings = context.watch<SettingsProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Settings')),
      body: ListView(
        padding: const EdgeInsets.only(bottom: 80),
        children: [
          const SizedBox(height: 16),
          _section('Appearance'),
          SwitchListTile(
            title: const Text('Dark Mode', style: TextStyle(color: XolericColors.textPrimary)),
            subtitle: const Text('Always dark', style: TextStyle(color: XolericColors.textTertiary)),
            value: settings.darkMode,
            onChanged: (v) => settings.darkMode = v,
            activeColor: XolericColors.neonCyan,
          ),
          _section('Accent Color'),
          ...AppConstants.accentOptions.map((name) => RadioListTile<String>(
            title: Text(name.toUpperCase(), style: const TextStyle(color: XolericColors.textPrimary)),
            value: name,
            groupValue: settings.accentColor,
            onChanged: (v) { if (v != null) settings.accentColor = v; },
            activeColor: XolericColors.neonCyan,
          )),
          _section('Playback'),
          SwitchListTile(
            title: const Text('Auto Play', style: TextStyle(color: XolericColors.textPrimary)),
            subtitle: const Text('Play next song automatically', style: TextStyle(color: XolericColors.textTertiary)),
            value: settings.autoPlay,
            onChanged: (v) => settings.autoPlay = v,
            activeColor: XolericColors.neonCyan,
          ),
          SwitchListTile(
            title: const Text('Mini Player', style: TextStyle(color: XolericColors.textPrimary)),
            subtitle: const Text('Show mini player bar', style: TextStyle(color: XolericColors.textTertiary)),
            value: settings.showMiniPlayer,
            onChanged: (v) => settings.showMiniPlayer = v,
            activeColor: XolericColors.neonCyan,
          ),
          _section('About'),
          const ListTile(
            title: Text('XOLERIC Music', style: TextStyle(color: XolericColors.textPrimary)),
            subtitle: Text('v1.0.0 - Professional Music Player', style: TextStyle(color: XolericColors.textTertiary)),
          ),
        ],
      ),
    );
  }

  Widget _section(String title) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
      child: Text(title.toUpperCase(), style: const TextStyle(color: XolericColors.neonCyan, fontSize: 12, fontWeight: FontWeight.w600, letterSpacing: 1)),
    );
  }
}
