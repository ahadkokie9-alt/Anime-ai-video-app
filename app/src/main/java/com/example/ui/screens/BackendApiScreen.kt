package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SleekBackground
import com.example.ui.theme.SleekCard
import com.example.ui.theme.SleekErrorRed
import com.example.ui.theme.SleekPurpleDark
import com.example.ui.theme.SleekPurpleGlow
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurplePrimary
import com.example.ui.theme.SleekSlate100
import com.example.ui.theme.SleekSlate200
import com.example.ui.theme.SleekSlate300
import com.example.ui.theme.SleekSlate400
import com.example.ui.theme.SleekSlate500
import com.example.ui.theme.SleekSlate800
import com.example.ui.theme.SleekSuccessGreen
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekSurfaceVariant
import com.example.viewmodel.StudioFormState

@Composable
fun BackendApiScreen(
    formState: StudioFormState,
    onUpdateReplicateTokenInput: (String) -> Unit,
    onSaveReplicateToken: () -> Unit,
    onUpdateHindiVoiceKeyInput: (String) -> Unit,
    onSaveHindiVoiceKey: () -> Unit,
    onClearKeys: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Header
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Backend API & AI Engine",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = SleekSlate100
                )
                Text(
                    text = "Configure live Replicate diffusion models & Hindi voice synthesis credentials",
                    style = MaterialTheme.typography.bodySmall,
                    color = SleekSlate400
                )
            }
        }

        item {
            // Replicate Configuration Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SleekCard)
                    .border(1.dp, SleekSlate800, RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .testTag("replicate_api_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SleekPurplePrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    tint = SleekPurpleLight,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Replicate Video API",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = SleekSlate100
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (formState.isSecretTokenConfigured) SleekSuccessGreen.copy(alpha = 0.15f)
                                    else SleekPurplePrimary.copy(alpha = 0.15f)
                                )
                                .border(
                                    1.dp,
                                    if (formState.isSecretTokenConfigured) SleekSuccessGreen.copy(alpha = 0.4f)
                                    else SleekPurpleGlow.copy(alpha = 0.3f),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (formState.isSecretTokenConfigured) "Configured (Live)" else "Ready to Connect",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (formState.isSecretTokenConfigured) SleekSuccessGreen else SleekPurpleGlow
                            )
                        }
                    }

                    Text(
                        text = "Used for multi-cut anime sequence generation. Tokens are read securely from the Secrets panel (REPLICATE_API_TOKEN) or can be entered below.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = SleekSlate400
                    )

                    OutlinedTextField(
                        value = formState.customReplicateTokenInput,
                        onValueChange = onUpdateReplicateTokenInput,
                        placeholder = {
                            Text(
                                text = if (formState.isSecretTokenConfigured) "•••••••••••••••• (Active)" else "r8_...",
                                color = SleekSlate500,
                                fontSize = 12.sp
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPurplePrimary,
                            unfocusedBorderColor = SleekSlate800,
                            focusedTextColor = SleekSlate100,
                            unfocusedTextColor = SleekSlate200,
                            focusedContainerColor = SleekSurface,
                            unfocusedContainerColor = SleekSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("replicate_token_text_field"),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onSaveReplicateToken() })
                    )

                    Button(
                        onClick = onSaveReplicateToken,
                        enabled = formState.customReplicateTokenInput.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = SleekPurplePrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_replicate_token_btn")
                    ) {
                        Text(
                            text = "Save Replicate Token",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {
            // Pipeline Architecture Summary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SleekCard)
                    .border(1.dp, SleekSlate800, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "ACTIVE PIPELINE STAGES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        ),
                        color = SleekSlate400
                    )

                    val pipelineSteps = listOf(
                        "1. Story Decomposition" to "Parses prompt into sequenced cinematic anime cuts with visual prompt expansion.",
                        "2. Character Appearance Lock" to "Injects consistency seed tokens across every keyframe shot.",
                        "3. Replicate Video Generation" to "Dispatches batch jobs to Cog/Replicate diffusion models.",
                        "4. Hindi Voice Synthesis" to "Synthesizes poetic narrator voice and character dialogues.",
                        "5. Dynamic Audio Mastering" to "Mixes Hindi speech with background soundtrack and sound effects.",
                        "6. Scene Stitching" to "Merges cuts with motion crossfades and encodes 9:16 vertical MP4 master."
                    )

                    pipelineSteps.forEach { (title, desc) ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(SleekPurpleLight)
                            )
                            Column {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = SleekSlate200
                                )
                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = SleekSlate400
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            // Clear / Reset
            if (formState.isSecretTokenConfigured) {
                OutlinedButton(
                    onClick = onClearKeys,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SleekSlate800)
                ) {
                    Text(
                        text = "Reset Stored API Keys",
                        style = MaterialTheme.typography.labelMedium,
                        color = SleekErrorRed
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
