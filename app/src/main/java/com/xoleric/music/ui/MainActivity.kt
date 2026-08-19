package com.xoleric.music.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.xoleric.music.XolericApp
import com.xoleric.music.core.ui.components.MiniPlayer
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericTheme
import com.xoleric.music.ui.navigation.BottomNavBar
import com.xoleric.music.ui.navigation.Screen
import com.xoleric.music.ui.navigation.XolericNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var hasPermission by mutableStateOf(false)
    private var isScanning by mutableStateOf(false)
    private var scanComplete by mutableStateOf(false)

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) startScan()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        hasPermission = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) startScan()

        setContent {
            XolericTheme {
                XolericApp(hasPermission, isScanning, scanComplete) {
                    permissionLauncher.launch(permission)
                }
            }
        }
    }

    private fun startScan() {
        isScanning = true
        val container = (application as XolericApp).container
        CoroutineScope(Dispatchers.IO).launch {
            container.musicRepository.scanAndSync()
            isScanning = false
            scanComplete = true
        }
    }
}

@Composable
fun XolericApp(hasPermission: Boolean, isScanning: Boolean, scanComplete: Boolean, onRequestPermission: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (!hasPermission) { PermissionScreen(onRequestPermission); return }
    if (isScanning && !scanComplete) { ScanningScreen(); return }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            XolericNavigation(navController = navController)
        }
        MiniPlayer(onClick = { navController.navigate(Screen.Player.route) })
        BottomNavBar(currentRoute = currentRoute) { route ->
            navController.navigate(route) {
                popUpTo(Screen.Home.route) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}

@Composable
fun PermissionScreen(onRequestPermission: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("XOLERIC", style = MaterialTheme.typography.displayLarge, color = XolericColors.NeonCyan)
            Text("Welcome to XOLERIC", style = MaterialTheme.typography.headlineMedium, color = XolericColors.TextPrimary)
            Text("Your music stays yours.", style = MaterialTheme.typography.bodyLarge, color = XolericColors.TextSecondary)
            Spacer(modifier = Modifier.height(32.dp))
            androidx.compose.material3.Button(
                onClick = onRequestPermission,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = XolericColors.NeonCyan, contentColor = XolericColors.Black)
            ) { Text("Grant Music Access", style = MaterialTheme.typography.labelLarge) }
        }
    }
}

@Composable
fun ScanningScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = XolericColors.NeonCyan)
            Text("Scanning your music\u2026", style = MaterialTheme.typography.bodyLarge, color = XolericColors.TextSecondary, modifier = Modifier.padding(top = 16.dp))
        }
    }
}
