package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SleekCard
import com.example.ui.theme.SleekPurpleGlow
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurplePrimary
import com.example.ui.theme.SleekSlate200
import com.example.ui.theme.SleekSlate300
import com.example.ui.theme.SleekSlate400
import com.example.ui.theme.SleekSlate500
import com.example.ui.theme.SleekSlate800
import com.example.ui.theme.SleekSurface
import com.example.viewmodel.AnimePromptTemplate
import com.example.viewmodel.SampleAnimePrompts

@Composable
fun PromptInputSection(
    promptText: String,
    onPromptChange: (String) -> Unit,
    onSelectTemplate: (AnimePromptTemplate) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Sleek Prompt Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SleekCard)
                .border(1.dp, SleekSlate800, RoundedCornerShape(16.dp))
                .padding(14.dp)
                .testTag("prompt_input_card")
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VIDEO PROMPT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = SleekSlate400
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(SleekPurplePrimary.copy(alpha = 0.2f))
                            .border(1.dp, SleekPurpleLight.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "AI Powered",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = SleekPurpleLight
                        )
                    }
                }

                // Text Input Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                ) {
                    if (promptText.isEmpty()) {
                        Text(
                            text = "Describe your story... e.g. In a vibrant cyberpunk landscape, a young girl discovers an ancient mechanical butterfly...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            ),
                            color = SleekSlate500
                        )
                    }

                    BasicTextField(
                        value = promptText,
                        onValueChange = onPromptChange,
                        textStyle = TextStyle(
                            color = SleekSlate200,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        ),
                        cursorBrush = SolidColor(SleekPurpleLight),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                            .testTag("story_prompt_input_field")
                    )
                }

                // Quick clear or count
                if (promptText.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${promptText.length} characters",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = SleekSlate500
                        )

                        Text(
                            text = "Clear",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SleekPurpleGlow
                            ),
                            modifier = Modifier
                                .clickable { onPromptChange("") }
                                .padding(2.dp)
                        )
                    }
                }
            }
        }

        // Quick Preset Inspiration Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SampleAnimePrompts.forEach { template ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SleekSurface)
                        .border(1.dp, SleekSlate800, RoundedCornerShape(8.dp))
                        .clickable { onSelectTemplate(template) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("prompt_template_${template.title.take(6)}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SleekPurpleLight,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = template.title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = SleekSlate300
                        )
                    }
                }
            }
        }
    }
}
