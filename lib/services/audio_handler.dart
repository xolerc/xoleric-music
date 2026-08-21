import 'package:audio_service/audio_service.dart';
import 'package:just_audio/just_audio.dart';
import 'package:flutter/foundation.dart';
import '../models/song.dart';

class XolericAudioHandler extends BaseAudioHandler with QueueHandler, SeekHandler {
  final AudioPlayer _player = AudioPlayer();
  AudioPlayer get player => _player;
  final List<SongModel> _songQueue = [];
  List<SongModel> get songQueue => _songQueue;
  int _currentIndex = -1;
  int get currentIndex => _currentIndex;
  SongModel? get currentSong => _currentIndex >= 0 && _currentIndex < _songQueue.length
      ? _songQueue[_currentIndex]
      : null;

  XolericAudioHandler() {
    _init();
  }

  void _init() {
    _player.playbackEventStream.map(_transformEvent).pipe(playbackState);
    _player.processingStateStream.listen((state) {
      if (state == ProcessingState.completed) {
        _onComplete();
      }
    });
  }

  void _onComplete() {
    if (_currentIndex < _songQueue.length - 1) {
      skipToNext();
    }
  }

  PlaybackState _transformEvent(PlaybackEvent event) {
    return PlaybackState(
      controls: [
        MediaControl.skipToPrevious,
        if (_player.playing) MediaControl.pause else MediaControl.play,
        MediaControl.skipToNext,
        MediaControl.stop,
      ],
      systemActions: const {
        MediaAction.seek,
        MediaAction.seekForward,
        MediaAction.seekBackward,
      },
      androidCompactActionIndices: const [0, 1, 2],
      processingState: const {
        ProcessingState.idle: AudioProcessingState.idle,
        ProcessingState.loading: AudioProcessingState.loading,
        ProcessingState.buffering: AudioProcessingState.buffering,
        ProcessingState.ready: AudioProcessingState.ready,
        ProcessingState.completed: AudioProcessingState.completed,
      }[_player.processingState]!,
      playing: _player.playing,
      updatePosition: _player.position,
      bufferedPosition: _player.bufferedPosition,
      speed: _player.speed,
      queueIndex: _currentIndex,
    );
  }

  void _updateMediaItem(SongModel song) {
    mediaItem.add(MediaItem(
      id: song.path,
      title: song.title,
      artist: song.artist,
      album: song.album,
      duration: Duration(milliseconds: song.duration),
    ));
  }

  Future<void> loadSong(SongModel song) async {
    try {
      await _player.setFilePath(song.path);
      _updateMediaItem(song);
    } catch (e) {
      debugPrint('loadSong error: $e');
    }
  }

  Future<void> loadQueue(List<SongModel> songs, int startIndex) async {
    _songQueue.clear();
    _songQueue.addAll(songs);
    _currentIndex = startIndex;
    if (startIndex >= 0 && startIndex < songs.length) {
      await loadSong(songs[startIndex]);
    }
  }

  Future<void> playSong(SongModel song, {List<SongModel>? queue}) async {
    if (queue != null) {
      final startIdx = queue.indexWhere((s) => s.id == song.id);
      await loadQueue(queue, startIdx >= 0 ? startIdx : 0);
    } else {
      final idx = _songQueue.indexWhere((s) => s.id == song.id);
      if (idx >= 0) {
        await loadSong(_songQueue[idx]);
        _currentIndex = idx;
      }
    }
    await _player.play();
  }

  @override
  Future<void> play() => _player.play();

  @override
  Future<void> pause() => _player.pause();

  @override
  Future<void> stop() async {
    await _player.stop();
    await super.stop();
  }

  @override
  Future<void> seek(Duration position) => _player.seek(position);

  @override
  Future<void> skipToNext() async {
    if (_currentIndex < _songQueue.length - 1) {
      _currentIndex++;
      await loadSong(_songQueue[_currentIndex]);
      await _player.play();
    }
  }

  @override
  Future<void> skipToPrevious() async {
    if (_currentIndex > 0) {
      _currentIndex--;
      await loadSong(_songQueue[_currentIndex]);
      await _player.play();
    }
  }

  Future<void> skipToIndex(int index) async {
    if (index >= 0 && index < _songQueue.length) {
      _currentIndex = index;
      await loadSong(_songQueue[index]);
      await _player.play();
    }
  }

  Future<void> togglePlay() async {
    if (_player.playing) {
      await _player.pause();
    } else {
      await _player.play();
    }
  }

  @override
  Future<void> onTaskRemoved() async {
    await stop();
    await _player.dispose();
  }

  @override
  Future<void> setRepeatMode(AudioServiceRepeatMode repeatMode) async {
    switch (repeatMode) {
      case AudioServiceRepeatMode.none:
        await _player.setLoopMode(LoopMode.off);
        break;
      case AudioServiceRepeatMode.one:
        await _player.setLoopMode(LoopMode.one);
        break;
      case AudioServiceRepeatMode.all:
      case AudioServiceRepeatMode.group:
        await _player.setLoopMode(LoopMode.all);
        break;
    }
  }

  @override
  Future<void> setShuffleMode(AudioServiceShuffleMode shuffleMode) async {
    final enabled = shuffleMode == AudioServiceShuffleMode.all;
    await _player.setShuffleModeEnabled(enabled);
  }

  void removeFromQueue(int index) {
    if (index >= 0 && index < _songQueue.length) {
      _songQueue.removeAt(index);
      if (index < _currentIndex) _currentIndex--;
      if (_currentIndex >= _songQueue.length) _currentIndex = _songQueue.length - 1;
    }
  }

  void reorderQueue(int oldIndex, int newIndex) {
    if (oldIndex < newIndex) newIndex--;
    final item = _songQueue.removeAt(oldIndex);
    _songQueue.insert(newIndex, item);
    if (_currentIndex == oldIndex) {
      _currentIndex = newIndex;
    } else if (_currentIndex > oldIndex && _currentIndex <= newIndex) {
      _currentIndex--;
    } else if (_currentIndex < oldIndex && _currentIndex >= newIndex) {
      _currentIndex++;
    }
  }
}
