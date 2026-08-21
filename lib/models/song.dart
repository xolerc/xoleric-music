class SongModel {
  final int id;
  final String title;
  final String artist;
  final String album;
  final int duration;
  final String path;
  final int size;
  final int dateAdded;
  final int? artworkId;

  const SongModel({
    required this.id,
    required this.title,
    required this.artist,
    required this.album,
    required this.duration,
    required this.path,
    required this.size,
    required this.dateAdded,
    this.artworkId,
  });

  String get durationText {
    final m = (duration ~/ 60000).toString().padLeft(2, '0');
    final s = ((duration ~/ 1000) % 60).toString().padLeft(2, '0');
    return '$m:$s';
  }
}
