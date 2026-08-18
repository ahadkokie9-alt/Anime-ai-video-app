package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Token
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AvailableAnimeEffects
import com.example.model.AvailableCameraMovements
import com.example.model.CameraMovementOption
import com.example.model.VideoAspectRatio
import com.example.model.VisualAnimeStyle
import com.example.ui.theme.SleekCard
import com.example.ui.theme.SleekPurpleGlow
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurplePrimary
import com.example.ui.theme.SleekSlate100
import com.example.ui.theme.SleekSlate200
import com.example.ui.theme.SleekSlate300
import com.example.ui.theme.SleekSlate400
import com.example.ui.theme.SleekSlate500
import com.example.ui.theme.SleekSlate600
import com.example.ui.theme.SleekSlate800
import com.example.ui.theme.SleekSurface

@Composable
fun StyleSelectorGrid(
    selectedStyle: VisualAnimeStyle,
    onSelectStyle: (VisualAnimeStyle) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "VISUAL STYLE",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = SleekSlate400,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val styles = listOf(
                VisualAnimeStyle.STYLE_2D_ANIME,
                VisualAnimeStyle.STYLE_3D_ANIME_CARTOON,
                VisualAnimeStyle.STYLE_CINEMATIC_ANIME,
                VisualAnimeStyle.STYLE_CINEMATIC_3D_ANIME
            )

            styles.forEach { style ->
                val isSelected = selectedStyle == style
                val icon = when (style) {
                    VisualAnimeStyle.STYLE_2D_ANIME -> Icons.Default.Palette
                    VisualAnimeStyle.STYLE_3D_ANIME_CARTOON -> Icons.Default.Token
                    VisualAnimeStyle.STYLE_CINEMATIC_ANIME -> Icons.Default.Movie
                    VisualAnimeStyle.STYLE_CINEMATIC_3D_ANIME -> Icons.Default.Layers
                }

                val shortName = when (style) {
                    VisualAnimeStyle.STYLE_2D_ANIME -> "2D Anime"
                    VisualAnimeStyle.STYLE_3D_ANIME_CARTOON -> "3D Cartoon"
                    VisualAnimeStyle.STYLE_CINEMATIC_ANIME -> "Cinematic"
                    VisualAnimeStyle.STYLE_CINEMATIC_3D_ANIME -> "3D Cinematic"
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) SleekPurplePrimary else SleekSurface)
                        .border(
                            1.dp,
                            if (isSelected) SleekPurpleLight.copy(alpha = 0.5f) else SleekSlate800,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectStyle(style) }
                        .padding(vertical = 10.dp, horizontal = 4.dp)
                        .testTag("style_card_${style.name}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = style.title,
                            tint = if (isSelected) Color.White else SleekSlate400,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = shortName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) Color.White else SleekSlate400,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DurationAndAspectControls(
    selectedDuration: Int,
    selectedAspectRatio: VideoAspectRatio,
    onSelectDuration: (Int) -> Unit,
    onSelectAspectRatio: (VideoAspectRatio) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDurationMenu by remember { mutableStateOf(false) }
    var showAspectMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Duration Card
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(SleekCard)
                .border(1.dp, SleekSlate800, RoundedCornerShape(16.dp))
                .clickable { showDurationMenu = true }
                .padding(12.dp)
                .testTag("duration_selector_card")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = SleekPurpleLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = "DURATION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = SleekSlate500
                        )
                        Text(
                            text = "$selectedDuration Seconds",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = SleekSlate200
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = SleekSlate600,
                    modifier = Modifier.size(16.dp)
                )
            }

            DropdownMenu(
                expanded = showDurationMenu,
                onDismissRequest = { showDurationMenu = false },
                modifier = Modifier.background(SleekSurface)
            ) {
                listOf(30, 45, 60, 90).forEach { seconds ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "$seconds Seconds ${if (seconds == 60) "(Standard Story)" else ""}",
                                color = if (seconds == selectedDuration) SleekPurpleLight else SleekSlate200
                            )
                        },
                        onClick = {
                            onSelectDuration(seconds)
                            showDurationMenu = false
                        }
                    )
                }
            }
        }

        // Aspect Ratio Card (9:16 Vertical Default)
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(SleekCard)
                .border(1.dp, SleekSlate800, RoundedCornerShape(16.dp))
                .clickable { showAspectMenu = true }
                .padding(12.dp)
                .testTag("aspect_ratio_selector_card")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AspectRatio,
                        contentDescription = null,
                        tint = SleekPurpleLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = "ASPECT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = SleekSlate500
                        )
                        Text(
                            text = selectedAspectRatio.ratioValue,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = SleekSlate200
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = SleekSlate600,
                    modifier = Modifier.size(16.dp)
                )
            }

            DropdownMenu(
                expanded = showAspectMenu,
                onDismissRequest = { showAspectMenu = false },
                modifier = Modifier.background(SleekSurface)
            ) {
                VideoAspectRatio.values().forEach { aspect ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${aspect.label} (${aspect.ratioValue})",
                                color = if (aspect == selectedAspectRatio) SleekPurpleLight else SleekSlate200
                            )
                        },
                        onClick = {
                            onSelectAspectRatio(aspect)
                            showAspectMenu = false
                        }
                    )
                }
            }
        }
    }
}
