import 'package:flutter/material.dart';
import '../../services/download_service.dart';

class DownloadProvider extends ChangeNotifier {
  final DownloadService _service = DownloadService();
  List<DownloadItem> _queue = [];

  List<DownloadItem> get queue => _queue;

  void init() {
    _service.queueStream.listen((queue) {
      _queue = queue;
      notifyListeners();
    });
  }

  Future<void> download(String url) async {
    await _service.download(url);
  }

  void pause(String id) => _service.pause(id);
  void resume(String id) => _service.resume(id);
  void cancel(String id) => _service.cancel(id);
  void retry(String id) => _service.retry(id);
}
