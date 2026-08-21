import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/theme_provider.dart';

class DownloadsScreen extends StatelessWidget {
  const DownloadsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final t = context.watch<ThemeProvider>().current;

    return Scaffold(
      backgroundColor: t.background,
      body: SafeArea(
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 16, 20, 8),
              child: Row(
                children: [
                  Container(
                    padding: const EdgeInsets.all(8),
                    decoration: BoxDecoration(
                      color: t.accent.withAlpha(30),
                      borderRadius: BorderRadius.circular(t.buttonRadius)),
                    child: Icon(Icons.download_rounded, color: t.accent, size: 24),
                  ),
                  const SizedBox(width: 12),
                  Text('Downloads', style: TextStyle(
                    color: t.textPrimary, fontSize: 22, fontWeight: FontWeight.bold)),
                ],
              ),
            ),
            Expanded(
              child: Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(Icons.cloud_download_outlined, size: 64,
                      color: t.textTertiary.withAlpha(100)),
                    const SizedBox(height: 16),
                    Text('No downloads yet', style: TextStyle(
                      color: t.textSecondary, fontSize: 16)),
                    const SizedBox(height: 8),
                    Text('Download songs from URLs', style: TextStyle(
                      color: t.textTertiary, fontSize: 13)),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
