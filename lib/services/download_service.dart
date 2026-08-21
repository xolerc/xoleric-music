import 'dart:async';
import 'dart:io';
import 'package:dio/dio.dart';
import 'package:path_provider/path_provider.dart';

enum DownloadStatus { queued, downloading, paused, completed, failed, cancelled }

class DownloadItem {
  final String id;
  final String url;
  final String fileName;
  final String fileType;
  double progress;
  int totalBytes;
  int receivedBytes;
  DownloadStatus status;
  CancelToken? cancelToken;

  DownloadItem({
    required this.id,
    required this.url,
    required this.fileName,
    this.fileType = 'other',
    this.progress = 0,
    this.totalBytes = 0,
    this.receivedBytes = 0,
    this.status = DownloadStatus.queued,
    this.cancelToken,
  });

  String get sizeText => _formatBytes(receivedBytes);
  String get totalSizeText => _formatBytes(totalBytes);

  static String _formatBytes(int bytes) {
    if (bytes < 1024) return '$bytes B';
    if (bytes < 1048576) return '${(bytes / 1024).toStringAsFixed(1)} KB';
    if (bytes < 1073741824) return '${(bytes / 1048576).toStringAsFixed(1)} MB';
    return '${(bytes / 1073741824).toStringAsFixed(1)} GB';
  }
}

class DownloadService {
  final Dio _dio = Dio();
  final List<DownloadItem> _queue = [];
  final _controller = StreamController<List<DownloadItem>>.broadcast();

  List<DownloadItem> get queue => List.unmodifiable(_queue);
  Stream<List<DownloadItem>> get queueStream => _controller.stream;

  String _guessFileType(String url) {
    final lower = url.toLowerCase();
    if (lower.endsWith('.mp3') || lower.endsWith('.flac') || lower.endsWith('.wav') || lower.endsWith('.m4a') || lower.contains('audio')) return 'music';
    if (lower.endsWith('.mp4') || lower.endsWith('.webm') || lower.endsWith('.mkv') || lower.contains('video')) return 'video';
    if (lower.endsWith('.jpg') || lower.endsWith('.jpeg') || lower.endsWith('.png') || lower.endsWith('.webp') || lower.contains('image')) return 'image';
    return 'other';
  }

  String _fileNameFromUrl(String url) {
    final uri = Uri.parse(url);
    final path = uri.path;
    final segments = path.split('/');
    return segments.isNotEmpty ? Uri.decodeComponent(segments.last) : 'download';
  }

  Future<void> download(String url) async {
    final item = DownloadItem(
      id: DateTime.now().millisecondsSinceEpoch.toString(),
      url: url,
      fileName: _fileNameFromUrl(url),
      fileType: _guessFileType(url),
      cancelToken: CancelToken(),
    );
    _queue.insert(0, item);
    _controller.add(_queue);
    _startDownload(item);
  }

  Future<void> _startDownload(DownloadItem item) async {
    item.status = DownloadStatus.downloading;
    _controller.add(_queue);

    try {
      final dir = await getApplicationDocumentsDirectory();
      final savePath = '${dir.path}/downloads/${item.fileName}';
      await Directory('${dir.path}/downloads').create(recursive: true);

      await _dio.download(
        item.url,
        savePath,
        cancelToken: item.cancelToken,
        onReceiveProgress: (received, total) {
          if (total > 0) {
            item.receivedBytes = received;
            item.totalBytes = total;
            item.progress = received / total;
            _controller.add(_queue);
          }
        },
      );
      item.status = DownloadStatus.completed;
    } on DioException catch (e) {
      if (e.type == DioExceptionType.cancel) {
        item.status = DownloadStatus.cancelled;
      } else {
        item.status = DownloadStatus.failed;
      }
    } catch (e) {
      item.status = DownloadStatus.failed;
    }
    _controller.add(_queue);
  }

  void pause(String id) {
    final item = _queue.firstWhere((i) => i.id == id, orElse: () => DownloadItem(id: '', url: '', fileName: ''));
    if (item.id.isNotEmpty && item.status == DownloadStatus.downloading) {
      item.cancelToken?.cancel();
      item.status = DownloadStatus.paused;
      _controller.add(_queue);
    }
  }

  void resume(String id) {
    final item = _queue.firstWhere((i) => i.id == id, orElse: () => DownloadItem(id: '', url: '', fileName: ''));
    if (item.id.isNotEmpty && item.status == DownloadStatus.paused) {
      item.cancelToken = CancelToken();
      item.status = DownloadStatus.queued;
      _startDownload(item);
    }
  }

  void cancel(String id) {
    final item = _queue.firstWhere((i) => i.id == id, orElse: () => DownloadItem(id: '', url: '', fileName: ''));
    if (item.id.isNotEmpty) {
      item.cancelToken?.cancel();
      item.status = DownloadStatus.cancelled;
      _controller.add(_queue);
    }
  }

  void retry(String id) {
    final item = _queue.firstWhere((i) => i.id == id, orElse: () => DownloadItem(id: '', url: '', fileName: ''));
    if (item.id.isNotEmpty && (item.status == DownloadStatus.failed || item.status == DownloadStatus.cancelled)) {
      item.cancelToken = CancelToken();
      item.progress = 0;
      item.receivedBytes = 0;
      item.status = DownloadStatus.queued;
      _startDownload(item);
    }
  }

  void dispose() {
    _controller.close();
    _dio.close();
  }
}
