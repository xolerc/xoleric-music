package com.xoleric.music.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.LocalAccents

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Library", Screen.Library.route, Icons.Filled.LibraryMusic, Icons.Outlined.LibraryMusic),
        BottomNavItem("Search", Screen.Search.route, Icons.Filled.Search, Icons.Outlined.Search),
        BottomNavItem("Settings", Screen.Settings.route, Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    NavigationBar(
        containerColor = XolericColors.BottomNavBg,
        contentColor = XolericColors.TextPrimary
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
                selected = selected,
                onClick = { onNavigate(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LocalAccents.current.accent,
                    selectedTextColor = LocalAccents.current.accent,
                    unselectedIconColor = XolericColors.TextTertiary,
                    unselectedTextColor = XolericColors.TextTertiary,
                    indicatorColor = LocalAccents.current.accentAlpha012
                )
            )
        }
    }
}
