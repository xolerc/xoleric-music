import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../providers/download_provider.dart';
import '../../utils/colors.dart';

class WebBrowserScreen extends StatefulWidget {
  const WebBrowserScreen({super.key});
  @override
  State<WebBrowserScreen> createState() => _WebBrowserScreenState();
}

class _WebBrowserScreenState extends State<WebBrowserScreen> {
  final _urlController = TextEditingController(text: 'https://');
  final _searchController = TextEditingController();

  @override
  void dispose() {
    _urlController.dispose();
    _searchController.dispose();
    super.dispose();
  }

  void _downloadUrl() {
    final url = _urlController.text.trim();
    if (url.isNotEmpty && url.startsWith('http')) {
      context.read<DownloadProvider>().download(url);
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Download started: ${url.split('/').last}'), backgroundColor: XolericColors.success),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Web Browser')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(12),
            child: Column(
              children: [
                TextField(
                  controller: _searchController,
                  style: const TextStyle(color: XolericColors.textPrimary),
                  decoration: InputDecoration(
                    hintText: 'Search for music, videos...',
                    hintStyle: const TextStyle(color: XolericColors.textTertiary),
                    prefixIcon: const Icon(Icons.search, color: XolericColors.textTertiary),
                    filled: true,
                    fillColor: XolericColors.surface,
                    border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
                  ),
                  onSubmitted: (_) {},
                ),
                const SizedBox(height: 8),
                Row(
                  children: [
                    Expanded(
                      child: TextField(
                        controller: _urlController,
                        style: const TextStyle(color: XolericColors.textPrimary, fontSize: 13),
                        decoration: InputDecoration(
                          hintText: 'Paste download URL here...',
                          hintStyle: const TextStyle(color: XolericColors.textTertiary),
                          prefixIcon: const Icon(Icons.link, color: XolericColors.textTertiary, size: 20),
                          filled: true,
                          fillColor: XolericColors.surface,
                          border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
                          contentPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 12),
                        ),
                      ),
                    ),
                    const SizedBox(width: 8),
                    Material(
                      color: XolericColors.neonCyan,
                      borderRadius: BorderRadius.circular(12),
                      child: InkWell(
                        borderRadius: BorderRadius.circular(12),
                        onTap: _downloadUrl,
                        child: const Padding(
                          padding: EdgeInsets.all(14),
                          child: Icon(Icons.download, color: XolericColors.black, size: 22),
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
          const Divider(color: XolericColors.divider),
          Expanded(
            child: Center(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Icon(Icons.language, size: 64, color: XolericColors.neonCyan.withValues(alpha: 0.2)),
                  const SizedBox(height: 16),
                  const Text('Paste a direct download link', style: TextStyle(color: XolericColors.textSecondary, fontSize: 15)),
                  const SizedBox(height: 8),
                  const Text(
                    'Supports: MP3, MP4, JPG, PNG, WEBP',
                    textAlign: TextAlign.center,
                    style: TextStyle(color: XolericColors.textTertiary, fontSize: 13),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
