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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SleekBackground
import com.example.ui.theme.SleekPurpleDark
import com.example.ui.theme.SleekPurpleGlow
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurplePrimary
import com.example.ui.theme.SleekSlate100
import com.example.ui.theme.SleekSlate300
import com.example.ui.theme.SleekSlate400
import com.example.ui.theme.SleekSlate800
import com.example.ui.theme.SleekSuccessGreen
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekWarningAmber

@Composable
fun AnimeHeaderBanner(
    isReplicateConfigured: Boolean,
    onOpenApiSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo & Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SleekPurplePrimary)
                    .border(1.dp, SleekPurpleGlow.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .testTag("app_logo_badge"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AnimeVideo AI",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AnimeVideo ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.3).sp
                        ),
                        color = SleekSlate100
                    )
                    Text(
                        text = "AI",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = SleekPurpleLight
                    )
                }
                Text(
                    text = "Cinematic 9:16 • Hindi Voices",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = SleekSlate400
                )
            }
        }

        // Action / Status Badges
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Replicate connection indicator
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isReplicateConfigured) SleekSuccessGreen.copy(alpha = 0.12f)
                        else SleekPurplePrimary.copy(alpha = 0.15f)
                    )
                    .border(
                        1.dp,
                        if (isReplicateConfigured) SleekSuccessGreen.copy(alpha = 0.4f)
                        else SleekPurpleGlow.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { onOpenApiSettings() }
                    .padding(horizontal = 8.dp, vertical = 5.dp)
                    .testTag("api_status_badge"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isReplicateConfigured) Icons.Default.CheckCircle else Icons.Default.Tune,
                        contentDescription = null,
                        tint = if (isReplicateConfigured) SleekSuccessGreen else SleekPurpleGlow,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isReplicateConfigured) "Replicate Ready" else "API Config",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (isReplicateConfigured) SleekSuccessGreen else SleekPurpleGlow
                    )
                }
            }

            // Sleek User avatar placeholder button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SleekSurface)
                    .border(1.dp, SleekSlate800, CircleShape)
                    .clickable { onOpenApiSettings() }
                    .testTag("profile_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Settings",
                    tint = SleekSlate300,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
