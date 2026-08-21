import 'dart:async';
import 'package:just_audio/just_audio.dart';
import '../../models/song.dart';

class AudioPlayerService {
  final AudioPlayer _player = AudioPlayer();
  List<SongModel> _queue = [];
  int _currentIndex = -1;

  AudioPlayer get player => _player;
  List<SongModel> get queue => _queue;
  int get currentIndex => _currentIndex;
  SongModel? get currentSong => _currentIndex >= 0 && _currentIndex < _queue.length ? _queue[_currentIndex] : null;
  Stream<Duration> get positionStream => _player.positionStream;
  Stream<Duration?> get durationStream => _player.durationStream;
  Stream<PlayerState> get playerStateStream => _player.playerStateStream;
  Stream<bool> get shuffleModeEnabledStream => _player.shuffleModeEnabledStream;
  Duration get position => _player.position;
  Duration? get duration => _player.duration;
  bool get playing => _player.playing;

  Future<void> play(SongModel song, {List<SongModel>? queue}) async {
    if (queue != null) {
      _queue = List.from(queue);
      _currentIndex = _queue.indexWhere((s) => s.id == song.id);
    } else {
      _currentIndex = _queue.indexWhere((s) => s.id == song.id);
      if (_currentIndex == -1) {
        _queue.add(song);
        _currentIndex = _queue.length - 1;
      }
    }

    try {
      await _player.setFilePath(song.path);
      await _player.play();
    } catch (e) {
      rethrow;
    }
  }

  Future<void> pause() async => _player.pause();
  Future<void> resume() async => _player.play();
  Future<void> togglePlay() async => _player.playing ? _player.pause() : _player.play();

  Future<void> seek(Duration position) async => _player.seek(position);

  Future<void> skipToNext() async {
    if (_currentIndex < _queue.length - 1) {
      await play(_queue[_currentIndex + 1]);
    }
  }

  Future<void> skipToPrevious() async {
    if (_currentIndex > 0) {
      await play(_queue[_currentIndex - 1]);
    }
  }

  Future<void> skipToIndex(int index) async {
    if (index >= 0 && index < _queue.length) {
      await play(_queue[index]);
    }
  }

  Future<void> setShuffleMode(bool enabled) async => _player.setShuffleModeEnabled(enabled);

  void cycleRepeatMode() {
    switch (_player.loopMode) {
      case LoopMode.off:
        _player.setLoopMode(LoopMode.all);
        break;
      case LoopMode.all:
        _player.setLoopMode(LoopMode.one);
        break;
      case LoopMode.one:
        _player.setLoopMode(LoopMode.off);
        break;
    }
  }

  void removeFromQueue(int index) {
    if (index >= 0 && index < _queue.length) {
      _queue.removeAt(index);
      if (index < _currentIndex) _currentIndex--;
      if (_currentIndex >= _queue.length) _currentIndex = _queue.length - 1;
    }
  }

  void reorderQueue(int oldIndex, int newIndex) {
    if (oldIndex < newIndex) newIndex--;
    final item = _queue.removeAt(oldIndex);
    _queue.insert(newIndex, item);
    if (_currentIndex == oldIndex) {
      _currentIndex = newIndex;
    } else if (_currentIndex > oldIndex && _currentIndex <= newIndex) {
      _currentIndex--;
    } else if (_currentIndex < oldIndex && _currentIndex >= newIndex) {
      _currentIndex++;
    }
  }

  Future<void> stop() async {
    await _player.stop();
    _queue.clear();
    _currentIndex = -1;
  }

  void dispose() {
    _player.dispose();
  }
}
