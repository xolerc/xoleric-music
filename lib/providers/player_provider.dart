import 'package:flutter/material.dart';
import 'package:audio_service/audio_service.dart';
import 'package:just_audio/just_audio.dart';
import '../models/song.dart';
import '../services/audio_handler.dart';
import '../globals.dart';
import '../utils/constants.dart';

class PlayerProvider extends ChangeNotifier {
  late final XolericAudioHandler _handler;
  AppRepeatMode _repeatMode = AppRepeatMode.off;

  PlayerProvider() {
    _handler = xolericAudioHandler;
    _handler.player.playbackEventStream.listen((_) => notifyListeners());
  }

  XolericAudioHandler get handler => _handler;
  AudioPlayer get player => _handler.player;
  SongModel? get currentSong => _handler.currentSong;
  bool get playing => _handler.player.playing;
  Duration get position => _handler.player.position;
  Duration? get duration => _handler.player.duration;
  List<SongModel> get queue => _handler.songQueue;
  int get currentIndex => _handler.currentIndex;
  bool get hasNext => currentIndex < queue.length - 1;
  bool get hasPrevious => currentIndex > 0;
  AppRepeatMode get repeatMode => _repeatMode;

  Future<void> playSong(SongModel song, {List<SongModel>? queue}) async {
    await _handler.playSong(song, queue: queue);
    notifyListeners();
  }

  Future<void> togglePlay() async {
    await _handler.togglePlay();
    notifyListeners();
  }

  Future<void> seek(Duration position) async {
    await _handler.seek(position);
  }

  Future<void> skipNext() async {
    await _handler.skipToNext();
    notifyListeners();
  }

  Future<void> skipPrevious() async {
    await _handler.skipToPrevious();
    notifyListeners();
  }

  Future<void> skipToIndex(int index) async {
    await _handler.skipToIndex(index);
    notifyListeners();
  }

  Future<void> toggleShuffle() async {
    final currentMode = player.shuffleModeEnabled;
    await _handler.setShuffleMode(
      currentMode ? AudioServiceShuffleMode.none : AudioServiceShuffleMode.all,
    );
    notifyListeners();
  }

  void toggleRepeat() {
    switch (_repeatMode) {
      case AppRepeatMode.off:
        _repeatMode = AppRepeatMode.all;
        _handler.setRepeatMode(AudioServiceRepeatMode.all);
        break;
      case AppRepeatMode.all:
        _repeatMode = AppRepeatMode.one;
        _handler.setRepeatMode(AudioServiceRepeatMode.one);
        break;
      case AppRepeatMode.one:
        _repeatMode = AppRepeatMode.off;
        _handler.setRepeatMode(AudioServiceRepeatMode.none);
        break;
    }
    notifyListeners();
  }

  void removeFromQueue(int index) {
    _handler.removeFromQueue(index);
    notifyListeners();
  }

  void reorderQueue(int oldIndex, int newIndex) {
    _handler.reorderQueue(oldIndex, newIndex);
    notifyListeners();
  }
}
