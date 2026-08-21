class AlbumModel {
  final int id;
  final String title;
  final String artist;
  final int? artworkId;
  final int songCount;

  const AlbumModel({
    required this.id,
    required this.title,
    required this.artist,
    this.artworkId,
    required this.songCount,
  });
}
