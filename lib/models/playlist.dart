class PlaylistModel {
  final String id;
  final String name;
  final List<int> songIds;
  final DateTime createdAt;

  const PlaylistModel({
    required this.id,
    required this.name,
    required this.songIds,
    required this.createdAt,
  });

  PlaylistModel copyWith({String? name, List<int>? songIds}) {
    return PlaylistModel(
      id: id,
      name: name ?? this.name,
      songIds: songIds ?? this.songIds,
      createdAt: createdAt,
    );
  }

  Map<String, dynamic> toJson() => {
    'id': id,
    'name': name,
    'songIds': songIds,
    'createdAt': createdAt.toIso8601String(),
  };

  factory PlaylistModel.fromJson(Map<String, dynamic> json) {
    return PlaylistModel(
      id: json['id'] as String,
      name: json['name'] as String,
      songIds: (json['songIds'] as List).cast<int>(),
      createdAt: DateTime.parse(json['createdAt'] as String),
    );
  }
}
