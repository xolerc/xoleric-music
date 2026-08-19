package com.xoleric.music.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.xoleric.music.core.ui.theme.XolericColors
import com.xoleric.music.core.ui.theme.XolericRadius
import com.xoleric.music.core.ui.theme.XolericSpacing
import com.xoleric.music.core.ui.theme.LocalAccents

@Composable
fun XolericGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = XolericRadius.lg,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .shadow(elevation = XolericSpacing.xs, shape = shape, ambientColor = Color.Black.copy(alpha = 0.3f), spotColor = Color.Black.copy(alpha = 0.3f))
            .clip(shape)
            .background(brush = Brush.verticalGradient(listOf(XolericColors.Glass, XolericColors.Glass.copy(alpha = 0.05f))))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        content()
    }
}

@Composable
fun XolericTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth().height(56.dp).padding(horizontal = XolericSpacing.lg),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (navigationIcon != null && onNavigationClick != null) {
            IconButton(onClick = onNavigationClick) {
                Icon(imageVector = navigationIcon, contentDescription = "Back", tint = XolericColors.TextPrimary)
            }
        } else { Box(modifier = Modifier.width(XolericSpacing.xl)) }
        Text(text = title, style = MaterialTheme.typography.titleLarge, color = XolericColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f).padding(horizontal = XolericSpacing.sm))
        if (actions != null) { actions() } else { Box(modifier = Modifier.width(XolericSpacing.xl)) }
    }
}

@Composable
fun XolericSectionHeader(title: String, modifier: Modifier = Modifier, action: String? = null, onActionClick: (() -> Unit)? = null) {
    Row(modifier = modifier.fillMaxWidth().padding(horizontal = XolericSpacing.lg, vertical = XolericSpacing.sm), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, color = XolericColors.TextPrimary)
        if (action != null && onActionClick != null) {
            Text(text = action, style = MaterialTheme.typography.labelLarge, color = LocalAccents.current.accent, modifier = Modifier.clickable { onActionClick() })
        }
    }
}

@Composable
fun XolericSongRow(
    title: String,
    artist: String = "",
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    duration: String? = null,
    isPlaying: Boolean = false,
    showIndex: Boolean = false,
    index: Int = 0,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val accentColor by animateColorAsState(targetValue = if (isPlaying) LocalAccents.current.accent else Color.Transparent, animationSpec = spring(stiffness = Spring.StiffnessLow))
    Row(modifier = modifier.fillMaxWidth().height(64.dp).clickable(onClick = onClick).padding(horizontal = XolericSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
        if (showIndex) {
            Text(text = "${index + 1}", style = MaterialTheme.typography.bodySmall, color = XolericColors.TextTertiary, modifier = Modifier.width(24.dp))
        } else {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(XolericRadius.sm)).background(brush = Brush.verticalGradient(listOf(XolericColors.SurfaceVariant, XolericColors.Surface))), contentAlignment = Alignment.Center) {
                Text(text = "♪", style = MaterialTheme.typography.titleMedium, color = XolericColors.TextTertiary)
            }
        }
        Column(modifier = Modifier.weight(1f).padding(start = XolericSpacing.md), verticalArrangement = Arrangement.Center) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, color = if (isPlaying) accentColor else XolericColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = subtitle ?: artist, style = MaterialTheme.typography.bodySmall, color = XolericColors.TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (duration != null) { Text(text = duration, style = MaterialTheme.typography.labelSmall, color = XolericColors.TextTertiary, modifier = Modifier.padding(start = XolericSpacing.sm)) }
        if (trailing != null) { trailing() }
    }
}

@Composable
fun XolericIconButton(icon: ImageVector, contentDescription: String, modifier: Modifier = Modifier, size: Dp = 48.dp, tint: Color = XolericColors.TextPrimary, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = modifier.size(size)) {
        Icon(imageVector = icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(size * 0.5f))
    }
}
