import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';
import '../../providers/download_provider.dart';
import '../../services/download_service.dart';
import '../../utils/colors.dart';
import '../../widgets/empty_state.dart';

class DownloadsScreen extends StatelessWidget {
  const DownloadsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final downloads = context.watch<DownloadProvider>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Downloads'),
        actions: [
          IconButton(
            icon: const Icon(Icons.language),
            onPressed: () => context.push('/browser'),
          ),
        ],
      ),
      body: downloads.queue.isEmpty
          ? EmptyState(
              icon: Icons.download,
              message: 'No downloads yet.\nUse the browser to find and download files.',
              actionLabel: 'Open Browser',
              onAction: () => context.push('/browser'),
            )
          : ListView.builder(
              padding: const EdgeInsets.only(bottom: 80),
              itemCount: downloads.queue.length,
              itemBuilder: (ctx, i) {
                final item = downloads.queue[i];
                return _DownloadTile(item: item, downloads: downloads);
              },
            ),
    );
  }
}

class _DownloadTile extends StatelessWidget {
  final DownloadItem item;
  final DownloadProvider downloads;

  const _DownloadTile({required this.item, required this.downloads});

  IconData _icon() {
    switch (item.fileType) {
      case 'music': return Icons.music_note;
      case 'video': return Icons.video_file;
      case 'image': return Icons.image;
      default: return Icons.file_download;
    }
  }

  Color _statusColor() {
    switch (item.status) {
      case DownloadStatus.downloading: return XolericColors.neonCyan;
      case DownloadStatus.completed: return XolericColors.success;
      case DownloadStatus.failed: return XolericColors.error;
      case DownloadStatus.paused: return XolericColors.neonBlue;
      case DownloadStatus.cancelled: return XolericColors.textTertiary;
      default: return XolericColors.textTertiary;
    }
  }

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Container(
        width: 44, height: 44,
        decoration: BoxDecoration(color: XolericColors.surfaceVariant, borderRadius: BorderRadius.circular(8)),
        child: Icon(_icon(), color: _statusColor(), size: 22),
      ),
      title: Text(item.fileName, maxLines: 1, overflow: TextOverflow.ellipsis,
          style: const TextStyle(color: XolericColors.textPrimary)),
      subtitle: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (item.status == DownloadStatus.downloading)
            Padding(
              padding: const EdgeInsets.only(top: 4),
              child: LinearProgressIndicator(value: item.progress, backgroundColor: XolericColors.surfaceVariant, color: XolericColors.neonCyan, minHeight: 3),
            ),
          Text('${item.status.name} - ${item.sizeText}/${item.totalSizeText}',
              style: TextStyle(color: _statusColor(), fontSize: 12)),
        ],
      ),
      trailing: _actions(),
    );
  }

  Widget? _actions() {
    switch (item.status) {
      case DownloadStatus.downloading:
        return Row(mainAxisSize: MainAxisSize.min, children: [
          IconButton(icon: const Icon(Icons.pause, size: 20), onPressed: () => downloads.pause(item.id)),
          IconButton(icon: const Icon(Icons.close, size: 20), onPressed: () => downloads.cancel(item.id)),
        ]);
      case DownloadStatus.paused:
        return IconButton(icon: const Icon(Icons.play_arrow, size: 20), onPressed: () => downloads.resume(item.id));
      case DownloadStatus.failed:
        return IconButton(icon: const Icon(Icons.refresh, size: 20), onPressed: () => downloads.retry(item.id));
      default:
        return null;
    }
  }
}
