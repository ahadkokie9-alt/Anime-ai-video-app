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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backend.pipeline.PipelineState
import com.example.data.AnimeProjectEntity
import com.example.model.GenerationStage
import com.example.ui.components.AnimeHeaderBanner
import com.example.ui.components.DurationAndAspectControls
import com.example.ui.components.GenerationProgressCard
import com.example.ui.components.PromptInputSection
import com.example.ui.components.StyleSelectorGrid
import com.example.ui.components.VideoPlayerPreviewCard
import com.example.ui.components.VoiceSettingsBottomSheet
import com.example.ui.components.VoiceSummaryCard
import com.example.ui.theme.SleekBackground
import com.example.ui.theme.SleekCard
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurplePrimary
import com.example.ui.theme.SleekSlate200
import com.example.ui.theme.SleekSlate400
import com.example.ui.theme.SleekSlate500
import com.example.ui.theme.SleekSlate800
import com.example.ui.theme.SleekSurface
import com.example.viewmodel.AnimeVideoViewModel
import com.example.viewmodel.StudioFormState

@Composable
fun StudioHomeScreen(
    viewModel: AnimeVideoViewModel,
    formState: StudioFormState,
    pipelineState: PipelineState,
    isPlaying: Boolean,
    playheadSeconds: Float,
    onOpenApiSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showVoiceModal by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Sleek Header
            AnimeHeaderBanner(
                isReplicateConfigured = formState.isSecretTokenConfigured,
                onOpenApiSettings = onOpenApiSettings
            )
        }

        // Active Generation Progress Card (glowing progress bar)
        if (pipelineState.currentStage != GenerationStage.IDLE) {
            item {
                GenerationProgressCard(
                    pipelineState = pipelineState,
                    onCancelGeneration = { viewModel.cancelVideoGeneration() },
                    onOpenApiConfig = onOpenApiSettings
                )
            }
        }

        // Completed video preview player
        if (pipelineState.currentStage == GenerationStage.COMPLETE) {
            item {
                val dummyProject = AnimeProjectEntity(
                    title = formState.storyPrompt.take(30).ifBlank { "Anime Masterpiece" },
                    storyPrompt = formState.storyPrompt,
                    visualStyle = formState.selectedStyle.title,
                    durationSeconds = formState.durationSeconds,
                    aspectRatio = formState.selectedAspectRatio.ratioValue,
                    characterConsistency = formState.characterConsistencyEnabled,
                    sceneContinuity = formState.sceneContinuityEnabled,
                    cameraMovement = formState.selectedCameraMovement.id,
                    lightingEffect = formState.selectedLightingEffect.id,
                    smoothTransitions = formState.smoothTransitionsEnabled,
                    narratorEnabled = formState.narratorEnabled,
                    narratorVoiceId = formState.selectedNarratorPreset.id,
                    narratorPitch = formState.narratorPitch,
                    narratorSpeed = formState.narratorSpeed,
                    bgmTrackId = formState.selectedBgmTrack.id,
                    bgmVolume = formState.bgmVolume,
                    sfxEnabled = true
                )

                VideoPlayerPreviewCard(
                    project = dummyProject,
                    isPlaying = isPlaying,
                    playheadSeconds = playheadSeconds,
                    onTogglePlay = { viewModel.togglePlayback() },
                    onSeek = { viewModel.seekPlayhead(it) },
                    onDownload = { /* handle download */ }
                )
            }
        }

        // 1. Story Prompt Input
        item {
            PromptInputSection(
                promptText = formState.storyPrompt,
                onPromptChange = { viewModel.updateStoryPrompt(it) },
                onSelectTemplate = { viewModel.applyPromptTemplate(it) }
            )
        }

        // 2. Visual Style Selector Grid (2D Anime, 3D Cartoon, Cinematic, Special)
        item {
            StyleSelectorGrid(
                selectedStyle = formState.selectedStyle,
                onSelectStyle = { viewModel.selectVisualStyle(it) }
            )
        }

        // 3. Duration & Aspect Ratio Row (60s, 9:16 Vertical)
        item {
            DurationAndAspectControls(
                selectedDuration = formState.durationSeconds,
                selectedAspectRatio = formState.selectedAspectRatio,
                onSelectDuration = { viewModel.selectDuration(it) },
                onSelectAspectRatio = { viewModel.selectAspectRatio(it) }
            )
        }

        // 4. Voice Settings Summary Card (Hindi Narrator + 2 Characters)
        item {
            VoiceSummaryCard(
                formState = formState,
                onConfigureClick = { showVoiceModal = true }
            )
        }

        // 5. Cinematic Consistency & Continuity Options
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SleekCard)
                    .border(1.dp, SleekSlate800, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "CONTINUITY & CINEMATICS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        ),
                        color = SleekSlate400
                    )

                    // Character Consistency Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = SleekPurpleLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "Character Consistency Lock",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = SleekSlate200
                                )
                                Text(
                                    text = "Preserves costumes, faces, and hairstyles across cuts",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = SleekSlate500
                                )
                            }
                        }

                        Switch(
                            checked = formState.characterConsistencyEnabled,
                            onCheckedChange = { viewModel.toggleCharacterConsistency(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SleekPurplePrimary,
                                uncheckedTrackColor = SleekSlate800
                            )
                        )
                    }

                    // Scene Continuity Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SlowMotionVideo,
                                contentDescription = null,
                                tint = SleekPurpleLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "Smooth Anime Transitions",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = SleekSlate200
                                )
                                Text(
                                    text = "Kinetic speed cuts and motion crossfades",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = SleekSlate500
                                )
                            }
                        }

                        Switch(
                            checked = formState.smoothTransitionsEnabled,
                            onCheckedChange = { viewModel.toggleSmoothTransitions(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SleekPurplePrimary,
                                uncheckedTrackColor = SleekSlate800
                            )
                        )
                    }
                }
            }
        }

        // 6. Generate Video Action Button (with sleek purple button & elevation)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Button(
                    onClick = { viewModel.startVideoGeneration() },
                    enabled = !pipelineState.isRunning && formState.storyPrompt.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("generate_video_main_btn"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekPurplePrimary,
                        disabledContainerColor = SleekSurface
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (pipelineState.isRunning) "GENERATING ANIME EPISODE..." else "GENERATE VIDEO",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Modal Voice Settings BottomSheet
    if (showVoiceModal) {
        VoiceSettingsBottomSheet(
            formState = formState,
            onToggleNarrator = { viewModel.toggleNarrator(it) },
            onSelectNarratorPreset = { viewModel.selectNarratorPreset(it) },
            onUpdateNarratorPitch = { viewModel.updateNarratorPitch(it) },
            onUpdateNarratorSpeed = { viewModel.updateNarratorSpeed(it) },
            onUpdateNarratorScript = { viewModel.updateNarratorSampleScript(it) },
            onAddCharacterVoice = { viewModel.addCharacterVoice() },
            onRemoveCharacterVoice = { viewModel.removeCharacterVoice(it) },
            onUpdateCharacterVoice = { viewModel.updateCharacterVoice(it) },
            onSelectBgmTrack = { viewModel.selectBgmTrack(it) },
            onUpdateBgmVolume = { viewModel.updateBgmVolume(it) },
            onDismiss = { showVoiceModal = false }
        )
    }
}
