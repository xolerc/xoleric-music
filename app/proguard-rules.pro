# ProGuard rules for XOLERIC Music Player

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-keep class com.xoleric.music.core.model.** { *; }

# Media3 / ExoPlayer
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# DataStore
-keep class androidx.datastore.** { *; }

# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# Compose
-dontwarn androidx.compose.**

# General
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
