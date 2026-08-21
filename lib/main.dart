import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'app/routes.dart';
import 'app/theme.dart';
import 'providers/music_provider.dart';
import 'providers/player_provider.dart';
import 'providers/playlist_provider.dart';
import 'providers/settings_provider.dart';
import 'providers/download_provider.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  SystemChrome.setSystemUIOverlayStyle(const SystemUiOverlayStyle(
    statusBarColor: Colors.transparent,
    statusBarIconBrightness: Brightness.light,
    systemNavigationBarColor: Color(0xE60A0A0A),
    systemNavigationBarIconBrightness: Brightness.light,
  ));

  runZonedGuarded(() {
    runApp(const XolericApp());
  }, (error, stack) {
    debugPrint('Uncaught error: $error');
  });
}

class XolericApp extends StatelessWidget {
  const XolericApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => SettingsProvider()..init()),
        ChangeNotifierProvider(create: (_) => MusicProvider()),
        ChangeNotifierProvider(create: (_) => PlayerProvider()),
        ChangeNotifierProvider(create: (_) => PlaylistProvider()..load()),
        ChangeNotifierProvider(create: (_) {
          final dp = DownloadProvider();
          dp.init();
          return dp;
        }),
      ],
      child: MaterialApp.router(
        title: 'XOLERIC',
        debugShowCheckedModeBanner: false,
        theme: XolericTheme.dark(),
        routerConfig: appRouter,
      ),
    );
  }
}
