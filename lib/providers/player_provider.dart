import 'dart:async';
import 'package:flutter/material.dart';
import 'package:just_audio/just_audio.dart';
import '../../models/song.dart';
import '../../services/audio_player_service.dart';
import '../../utils/constants.dart';

class PlayerProvider extends ChangeNotifier {
  final AudioPlayerService _audioService = AudioPlayerService();
  AppRepeatMode _repeatMode = AppRepeatMode.off;

  AudioPlayerService get audioService => _audioService;
  SongModel? get currentSong => _audioService.currentSong;
  bool get playing => _audioService.playing;
  Duration get position => _audioService.position;
  Duration? get duration => _audioService.duration;
  List<SongModel> get queue => _audioService.queue;
  int get currentIndex => _audioService.currentIndex;
  bool get hasNext => _audioService.currentIndex < _audioService.queue.length - 1;
  bool get hasPrevious => _audioService.currentIndex > 0;
  AppRepeatMode get repeatMode => _repeatMode;

  StreamSubscription? _stateSub;

  PlayerProvider() {
    _stateSub = _audioService.playerStateStream.listen((state) {
      if (state.processingState == ProcessingState.completed) {
        _handleSongComplete();
      }
      notifyListeners();
    });
  }

  void _handleSongComplete() {
    switch (_repeatMode) {
      case AppRepeatMode.one:
        _audioService.seek(Duration.zero);
        _audioService.resume();
        break;
      case AppRepeatMode.all:
        if (_audioService.currentIndex < _audioService.queue.length - 1) {
          _audioService.skipToNext();
        } else {
          _audioService.skipToIndex(0);
        }
        break;
      case AppRepeatMode.off:
        if (_audioService.currentIndex < _audioService.queue.length - 1) {
          _audioService.skipToNext();
        }
        break;
    }
  }

  Future<void> playSong(SongModel song, {List<SongModel>? queue}) async {
    await _audioService.play(song, queue: queue);
    notifyListeners();
  }

  Future<void> togglePlay() async {
    await _audioService.togglePlay();
    notifyListeners();
  }

  Future<void> seek(Duration position) async {
    await _audioService.seek(position);
  }

  Future<void> skipNext() async {
    await _audioService.skipToNext();
    notifyListeners();
  }

  Future<void> skipPrevious() async {
    await _audioService.skipToPrevious();
    notifyListeners();
  }

  Future<void> skipToIndex(int index) async {
    await _audioService.skipToIndex(index);
    notifyListeners();
  }

  Future<void> toggleShuffle() async {
    await _audioService.setShuffleMode(!_audioService.player.shuffleModeEnabled);
    notifyListeners();
  }

  void toggleRepeat() {
    switch (_repeatMode) {
      case AppRepeatMode.off:
        _repeatMode = AppRepeatMode.all;
        _audioService.player.setLoopMode(LoopMode.all);
        break;
      case AppRepeatMode.all:
        _repeatMode = AppRepeatMode.one;
        _audioService.player.setLoopMode(LoopMode.one);
        break;
      case AppRepeatMode.one:
        _repeatMode = AppRepeatMode.off;
        _audioService.player.setLoopMode(LoopMode.off);
        break;
    }
    notifyListeners();
  }

  void removeFromQueue(int index) {
    _audioService.removeFromQueue(index);
    notifyListeners();
  }

  void reorderQueue(int oldIndex, int newIndex) {
    _audioService.reorderQueue(oldIndex, newIndex);
    notifyListeners();
  }

  @override
  void dispose() {
    _stateSub?.cancel();
    _audioService.dispose();
    super.dispose();
  }
}
