import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import '../screens/home/home_screen.dart';
import '../screens/library/albums_screen.dart';
import '../screens/library/artists_screen.dart';
import '../screens/library/genres_screen.dart';
import '../screens/library/folders_screen.dart';
import '../screens/library/favorites_screen.dart';
import '../screens/player/player_screen.dart';
import '../screens/search/search_screen.dart';
import '../screens/settings/settings_screen.dart';
import '../screens/playlists/playlists_screen.dart';
import '../screens/playlists/playlist_detail_screen.dart';
import '../screens/player/queue_screen.dart';
import '../screens/player/lyrics_screen.dart';
import '../screens/downloads/downloads_screen.dart';
import '../screens/downloads/web_browser_screen.dart';
import '../widgets/bottom_nav_shell.dart';

final GlobalKey<NavigatorState> _rootNavKey = GlobalKey<NavigatorState>();
final GlobalKey<NavigatorState> _shellNavKey = GlobalKey<NavigatorState>();

final GoRouter appRouter = GoRouter(
  navigatorKey: _rootNavKey,
  initialLocation: '/home',
  routes: [
    ShellRoute(
      navigatorKey: _shellNavKey,
      builder: (context, state, child) => BottomNavShell(child: child),
      routes: [
        GoRoute(path: '/home', builder: (_, __) => const HomeScreen()),
        GoRoute(path: '/library', builder: (_, __) => const HomeScreen()),
        GoRoute(path: '/search', builder: (_, __) => const SearchScreen()),
        GoRoute(path: '/settings', builder: (_, __) => const SettingsScreen()),
      ],
    ),
    GoRoute(path: '/player', builder: (_, __) => const PlayerScreen()),
    GoRoute(path: '/queue', builder: (_, __) => const QueueScreen()),
    GoRoute(path: '/lyrics', builder: (_, __) => const LyricsScreen()),
    GoRoute(path: '/albums', builder: (_, __) => const AlbumsScreen()),
    GoRoute(path: '/artists', builder: (_, __) => const ArtistsScreen()),
    GoRoute(path: '/genres', builder: (_, __) => const GenresScreen()),
    GoRoute(path: '/folders', builder: (_, __) => const FoldersScreen()),
    GoRoute(path: '/favorites', builder: (_, __) => const FavoritesScreen()),
    GoRoute(path: '/playlists', builder: (_, __) => const PlaylistsScreen()),
    GoRoute(path: '/playlist/:id', builder: (ctx, state) => PlaylistDetailScreen(playlistId: state.pathParameters['id']!)),
    GoRoute(path: '/downloads', builder: (_, __) => const DownloadsScreen()),
    GoRoute(path: '/browser', builder: (_, __) => const WebBrowserScreen()),
  ],
);
