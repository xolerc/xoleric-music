import 'package:audio_service/audio_service.dart';
import 'services/audio_handler.dart';

late XolericAudioHandler xolericAudioHandler;

Future<void> initAudioService() async {
  xolericAudioHandler = await AudioService.init(
    builder: () => XolericAudioHandler(),
    config: const AudioServiceConfig(
      androidNotificationChannelId: 'com.xoleric.music.channel.audio',
      androidNotificationChannelName: 'XOLERIC Music',
      androidNotificationOngoing: true,
      androidStopForegroundOnPause: true,
      androidNotificationIcon: 'mipmap/ic_launcher',
      fastForwardInterval: Duration(seconds: 10),
      rewindInterval: Duration(seconds: 10),
    ),
  );
}
