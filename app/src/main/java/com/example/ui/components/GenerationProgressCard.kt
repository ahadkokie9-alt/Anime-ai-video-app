package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.backend.pipeline.PipelineState
import com.example.model.GenerationStage
import com.example.ui.theme.SleekBackground
import com.example.ui.theme.SleekErrorRed
import com.example.ui.theme.SleekPurpleDark
import com.example.ui.theme.SleekPurpleGlow
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurplePrimary
import com.example.ui.theme.SleekSlate100
import com.example.ui.theme.SleekSlate300
import com.example.ui.theme.SleekSlate400
import com.example.ui.theme.SleekSlate500
import com.example.ui.theme.SleekSlate800
import com.example.ui.theme.SleekSuccessGreen
import com.example.ui.theme.SleekSurface
import com.example.ui.theme.SleekWarningAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenerationProgressCard(
    pipelineState: PipelineState,
    onCancelGeneration: () -> Unit,
    onOpenApiConfig: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (pipelineState.currentStage == GenerationStage.IDLE) {
        return
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val animatedProgress by animateFloatAsState(
        targetValue = (pipelineState.progressPercent.coerceIn(0, 100)) / 100f,
        animationSpec = tween(400),
        label = "progressBar"
    )

    val isError = pipelineState.currentStage == GenerationStage.FAILED
    val isComplete = pipelineState.currentStage == GenerationStage.COMPLETE

    val cardBorderColor = when {
        isError -> SleekErrorRed.copy(alpha = 0.5f)
        isComplete -> SleekSuccessGreen.copy(alpha = 0.6f)
        else -> SleekPurpleLight.copy(alpha = 0.35f)
    }

    val cardBgColor = when {
        isError -> SleekErrorRed.copy(alpha = 0.08f)
        isComplete -> SleekSuccessGreen.copy(alpha = 0.08f)
        else -> SleekPurpleDark.copy(alpha = 0.25f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBgColor)
            .border(1.dp, cardBorderColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("generation_progress_card")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row: Status & Percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (pipelineState.isRunning) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SleekPurpleLight.copy(alpha = pulseAlpha))
                        )
                    } else if (isComplete) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SleekSuccessGreen,
                            modifier = Modifier.size(14.dp)
                        )
                    } else if (isError) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = SleekErrorRed,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Text(
                        text = when {
                            isError -> "Pipeline Paused"
                            isComplete -> "Generation Complete"
                            else -> "${pipelineState.currentStage.title}..."
                        }.uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        ),
                        color = when {
                            isError -> SleekErrorRed
                            isComplete -> SleekSuccessGreen
                            else -> SleekPurpleGlow
                        }
                    )
                }

                Text(
                    text = "${pipelineState.progressPercent}%",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    ),
                    color = when {
                        isError -> SleekErrorRed
                        isComplete -> SleekSuccessGreen
                        else -> SleekPurpleGlow
                    }
                )
            }

            // Sleek Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(SleekSlate800)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    if (isError) SleekErrorRed else SleekPurplePrimary,
                                    if (isError) SleekErrorRed else SleekPurpleGlow
                                )
                            )
                        )
                )
            }

            // Detailed Status Message
            Text(
                text = pipelineState.statusMessage,
                style = MaterialTheme.typography.bodySmall,
                color = SleekSlate300,
                maxLines = 2
            )

            // Stage Checkpoints Grid (7 Stages)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val stages = listOf(
                    GenerationStage.PREPARING,
                    GenerationStage.GENERATING_SCENES,
                    GenerationStage.GENERATING_HINDI_VOICES,
                    GenerationStage.PROCESSING_AUDIO,
                    GenerationStage.COMBINING_SCENES,
                    GenerationStage.FINALIZING_VIDEO,
                    GenerationStage.COMPLETE
                )

                stages.forEach { stage ->
                    val isPast = pipelineState.currentStage.stageNumber > stage.stageNumber || isComplete
                    val isCurrent = pipelineState.currentStage == stage

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = when {
                                isPast -> Icons.Default.CheckCircle
                                isCurrent -> Icons.Default.CheckCircle
                                else -> Icons.Default.RadioButtonUnchecked
                            },
                            contentDescription = null,
                            tint = when {
                                isPast -> SleekSuccessGreen
                                isCurrent -> SleekPurpleGlow
                                else -> SleekSlate500
                            },
                            modifier = Modifier.size(11.dp)
                        )

                        Text(
                            text = stage.title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = when {
                                isPast -> SleekSlate300
                                isCurrent -> SleekPurpleLight
                                else -> SleekSlate500
                            }
                        )
                    }
                }
            }

            // Error / Config Required Action Area
            if (pipelineState.isConfigRequired || isError) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SleekBackground.copy(alpha = 0.7f))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = pipelineState.errorDetails ?: "Real backend provider API key required to invoke live Replicate inference.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = SleekSlate300
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onOpenApiConfig,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SleekPurplePrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("configure_api_key_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Configure Replicate API",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }

                        OutlinedButton(
                            onClick = onCancelGeneration,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SleekSlate800)
                        ) {
                            Text(
                                text = "Dismiss",
                                style = MaterialTheme.typography.labelSmall,
                                color = SleekSlate400
                            )
                        }
                    }
                }
            }

            // Cancel action while running
            if (pipelineState.isRunning) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Cancel Generation",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = SleekSlate400
                        ),
                        modifier = Modifier
                            .clickable { onCancelGeneration() }
                            .padding(4.dp)
                            .testTag("cancel_generation_text_btn")
                    )
                }
            }
        }
    }
}
