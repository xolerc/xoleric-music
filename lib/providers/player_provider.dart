import 'package:flutter/material.dart';
import 'package:audio_service/audio_service.dart';
import 'package:just_audio/just_audio.dart';
import '../models/song.dart';
import '../services/audio_handler.dart';
import '../globals.dart';
import '../utils/constants.dart';

class PlayerProvider extends ChangeNotifier {
  XolericAudioHandler? _handler;
  AppRepeatMode _repeatMode = AppRepeatMode.off;

  PlayerProvider() {
    _tryInit();
  }

  void _tryInit() {
    try {
      _handler = xolericAudioHandler;
      _handler!.player.playbackEventStream.listen((_) => notifyListeners());
    } catch (e) {
      debugPrint('PlayerProvider init failed: $e');
    }
  }

  XolericAudioHandler get handler => _handler!;
  AudioPlayer get player => _handler?.player ?? AudioPlayer();
  SongModel? get currentSong => _handler?.currentSong;
  bool get playing => _handler?.player.playing ?? false;
  Duration get position => _handler?.player.position ?? Duration.zero;
  Duration? get duration => _handler?.player.duration;
  List<SongModel> get queue => _handler?.songQueue ?? [];
  int get currentIndex => _handler?.currentIndex ?? -1;
  bool get hasNext => currentIndex >= 0 && currentIndex < queue.length - 1;
  bool get hasPrevious => currentIndex > 0;
  AppRepeatMode get repeatMode => _repeatMode;

  Future<void> playSong(SongModel song, {List<SongModel>? queueList}) async {
    if (_handler == null) return;
    await _handler!.playSong(song, queue: queueList);
    notifyListeners();
  }

  Future<void> togglePlay() async {
    if (_handler == null) return;
    await _handler!.togglePlay();
    notifyListeners();
  }

  Future<void> seek(Duration position) async {
    await _handler?.seek(position);
  }

  Future<void> skipNext() async {
    await _handler?.skipToNext();
    notifyListeners();
  }

  Future<void> skipPrevious() async {
    await _handler?.skipToPrevious();
    notifyListeners();
  }

  Future<void> skipToIndex(int index) async {
    await _handler?.skipToIndex(index);
    notifyListeners();
  }

  Future<void> toggleShuffle() async {
    if (_handler == null) return;
    final currentMode = player.shuffleModeEnabled;
    await _handler!.setShuffleMode(
      currentMode ? AudioServiceShuffleMode.none : AudioServiceShuffleMode.all,
    );
    notifyListeners();
  }

  void toggleRepeat() {
    switch (_repeatMode) {
      case AppRepeatMode.off:
        _repeatMode = AppRepeatMode.all;
        _handler?.setRepeatMode(AudioServiceRepeatMode.all);
        break;
      case AppRepeatMode.all:
        _repeatMode = AppRepeatMode.one;
        _handler?.setRepeatMode(AudioServiceRepeatMode.one);
        break;
      case AppRepeatMode.one:
        _repeatMode = AppRepeatMode.off;
        _handler?.setRepeatMode(AudioServiceRepeatMode.none);
        break;
    }
    notifyListeners();
  }

  void removeFromQueue(int index) {
    _handler?.removeFromQueue(index);
    notifyListeners();
  }

  void reorderQueue(int oldIndex, int newIndex) {
    _handler?.reorderQueue(oldIndex, newIndex);
    notifyListeners();
  }
}
