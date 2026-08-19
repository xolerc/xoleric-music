# XOLERIC

Premium offline music player for Android. Built with Kotlin, Jetpack Compose, Media3/ExoPlayer, and Room.

## Features

- Dark glassmorphism UI with neon accent colors
- Full background playback with MediaSession integration
- Multi-queue system
- Smart playlists (Recently Played, Most Played, Favorites, Never Played)
- Library browsing by Albums, Artists, Genres, Folders
- Sleep timer
- Shuffle and repeat modes
- Search across songs, artists, albums
- Equalizer with presets
- Lyrics display
- Settings with accent color customization
- DataStore-based preferences
- Room database for persistent data
- MediaStore scanning for music discovery

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material3
- **Architecture:** MVVM + Manual DI
- **Playback:** Media3/ExoPlayer + MediaSession
- **Database:** Room
- **Preferences:** DataStore
- **Image Loading:** Coil
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 35

## Build

```bash
# Debug build
./gradlew assembleDebug

# Release build (requires signing config)
./gradlew assembleRelease
```

## Installation

Download the latest APK from [Releases](../../releases).

## License

MIT License
