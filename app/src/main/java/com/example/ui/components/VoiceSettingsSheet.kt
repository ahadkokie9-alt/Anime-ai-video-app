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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.AvailableMusicTracks
import com.example.model.BackgroundMusicTrack
import com.example.model.HindiCharacterPresets
import com.example.model.HindiCharacterVoiceConfig
import com.example.model.HindiNarratorPresets
import com.example.model.HindiVoicePreset
import com.example.ui.theme.SleekBackground
import com.example.ui.theme.SleekCard
import com.example.ui.theme.SleekPurpleGlow
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurplePrimary
import com.example.ui.theme.SleekSlate100
import com.example.ui.theme.SleekSlate200
import com.example.ui.theme.SleekSlate300
import com.example.ui.theme.SleekSlate400
import com.example.ui.theme.SleekSlate500
import com.example.ui.theme.SleekSlate800
import com.example.ui.theme.SleekSurface
import com.example.viewmodel.StudioFormState

@Composable
fun VoiceSummaryCard(
    formState: StudioFormState,
    onConfigureClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val charCount = formState.characterVoices.size
    val voiceDesc = buildString {
        if (formState.narratorEnabled) {
            append("Hindi Narrator (${formState.selectedNarratorPreset.name.take(12)})")
            if (charCount > 0) append(" + $charCount Character${if (charCount > 1) "s" else ""}")
        } else if (charCount > 0) {
            append("$charCount Character Voice${if (charCount > 1) "s" else ""}")
        } else {
            append("Music & SFX Only")
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SleekCard)
            .border(1.dp, SleekSlate800, RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag("voice_settings_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SleekPurplePrimary.copy(alpha = 0.15f))
                        .border(1.dp, SleekPurpleGlow.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = "Voice Settings",
                        tint = SleekPurpleLight,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "VOICE & AUDIO ENGINE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = SleekSlate500
                    )
                    Text(
                        text = voiceDesc,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = SleekSlate200,
                        maxLines = 1
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SleekSurface)
                    .clickable { onConfigureClick() }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("configure_voice_btn"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CONFIGURE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.4.sp
                    ),
                    color = SleekSlate300
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceSettingsBottomSheet(
    formState: StudioFormState,
    onToggleNarrator: (Boolean) -> Unit,
    onSelectNarratorPreset: (HindiVoicePreset) -> Unit,
    onUpdateNarratorPitch: (Float) -> Unit,
    onUpdateNarratorSpeed: (Float) -> Unit,
    onUpdateNarratorScript: (String) -> Unit,
    onAddCharacterVoice: () -> Unit,
    onRemoveCharacterVoice: (String) -> Unit,
    onUpdateCharacterVoice: (HindiCharacterVoiceConfig) -> Unit,
    onSelectBgmTrack: (BackgroundMusicTrack) -> Unit,
    onUpdateBgmVolume: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableIntStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SleekBackground,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 6.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(SleekSlate800)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Sheet Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hindi Voice & Sound Engine",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SleekSlate100
                    )
                    Text(
                        text = "Separate Narrator + Multi-Character Voice Cast",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = SleekSlate400
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = SleekSlate400
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sub-tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SleekSurface,
                contentColor = SleekPurpleLight,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = SleekPurplePrimary
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, SleekSlate800, RoundedCornerShape(10.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Hindi Narrator",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (selectedTab == 0) SleekPurpleLight else SleekSlate400
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Characters (${formState.characterVoices.size})",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (selectedTab == 1) SleekPurpleLight else SleekSlate400
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = "BGM & SFX",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (selectedTab == 2) SleekPurpleLight else SleekSlate400
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Content
            when (selectedTab) {
                0 -> NarratorSettingsTab(
                    formState = formState,
                    onToggleNarrator = onToggleNarrator,
                    onSelectPreset = onSelectNarratorPreset,
                    onUpdatePitch = onUpdateNarratorPitch,
                    onUpdateSpeed = onUpdateNarratorSpeed,
                    onUpdateScript = onUpdateNarratorScript
                )
                1 -> CharacterVoicesTab(
                    characters = formState.characterVoices,
                    onAddCharacter = onAddCharacterVoice,
                    onRemoveCharacter = onRemoveCharacterVoice,
                    onUpdateCharacter = onUpdateCharacterVoice
                )
                2 -> BgmAndSfxTab(
                    selectedBgm = formState.selectedBgmTrack,
                    bgmVolume = formState.bgmVolume,
                    onSelectBgm = onSelectBgmTrack,
                    onUpdateVolume = onUpdateBgmVolume
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("apply_voice_settings_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = SleekPurplePrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Apply Voice Configuration",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NarratorSettingsTab(
    formState: StudioFormState,
    onToggleNarrator: (Boolean) -> Unit,
    onSelectPreset: (HindiVoicePreset) -> Unit,
    onUpdatePitch: (Float) -> Unit,
    onUpdateSpeed: (Float) -> Unit,
    onUpdateScript: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Enable toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SleekCard)
                    .border(1.dp, SleekSlate800, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Enable Hindi Narrator Voice",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = SleekSlate100
                    )
                    Text(
                        text = "Synthesizes poetic cinematic voiceover for scenes",
                        style = MaterialTheme.typography.labelSmall,
                        color = SleekSlate400
                    )
                }

                Switch(
                    checked = formState.narratorEnabled,
                    onCheckedChange = onToggleNarrator,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = SleekPurplePrimary,
                        uncheckedTrackColor = SleekSlate800
                    )
                )
            }
        }

        if (formState.narratorEnabled) {
            item {
                Text(
                    text = "SELECT HINDI NARRATOR PRESET",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = SleekSlate400
                )
            }

            items(HindiNarratorPresets) { preset ->
                val isSelected = formState.selectedNarratorPreset.id == preset.id
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) SleekPurplePrimary.copy(alpha = 0.15f) else SleekCard)
                        .border(
                            1.dp,
                            if (isSelected) SleekPurpleLight else SleekSlate800,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectPreset(preset) }
                        .padding(12.dp)
                        .testTag("narrator_preset_${preset.id}")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${preset.name} (${preset.tone})",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSelected) SleekPurpleLight else SleekSlate200
                            )
                            Text(
                                text = preset.sampleTextHindi,
                                style = MaterialTheme.typography.labelSmall,
                                color = SleekSlate400,
                                maxLines = 1
                            )
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(SleekPurplePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Pitch & Speed Sliders
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SleekCard)
                        .border(1.dp, SleekSlate800, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Voice Pitch",
                            style = MaterialTheme.typography.labelSmall,
                            color = SleekSlate300
                        )
                        Text(
                            text = "%.2fx".format(formState.narratorPitch),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SleekPurpleLight
                        )
                    }
                    Slider(
                        value = formState.narratorPitch,
                        onValueChange = onUpdatePitch,
                        valueRange = 0.7f..1.3f,
                        colors = SliderDefaults.colors(
                            thumbColor = SleekPurpleLight,
                            activeTrackColor = SleekPurplePrimary,
                            inactiveTrackColor = SleekSlate800
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Speech Speed",
                            style = MaterialTheme.typography.labelSmall,
                            color = SleekSlate300
                        )
                        Text(
                            text = "%.2fx".format(formState.narratorSpeed),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SleekPurpleLight
                        )
                    }
                    Slider(
                        value = formState.narratorSpeed,
                        onValueChange = onUpdateSpeed,
                        valueRange = 0.7f..1.3f,
                        colors = SliderDefaults.colors(
                            thumbColor = SleekPurpleLight,
                            activeTrackColor = SleekPurplePrimary,
                            inactiveTrackColor = SleekSlate800
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterVoicesTab(
    characters: List<HindiCharacterVoiceConfig>,
    onAddCharacter: () -> Unit,
    onRemoveCharacter: (String) -> Unit,
    onUpdateCharacter: (HindiCharacterVoiceConfig) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ANIME CAST DIALOGUE VOICES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = SleekSlate400
                )

                Button(
                    onClick = onAddCharacter,
                    colors = ButtonDefaults.buttonColors(containerColor = SleekSurface),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("add_character_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = SleekPurpleLight
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Character",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = SleekPurpleLight
                    )
                }
            }
        }

        items(characters) { charConfig ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SleekCard)
                    .border(1.dp, SleekSlate800, RoundedCornerShape(12.dp))
                    .padding(12.dp)
                    .testTag("character_card_${charConfig.id}")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(SleekPurplePrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = SleekPurpleLight,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = charConfig.characterName,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = SleekSlate100
                            )
                        }

                        IconButton(
                            onClick = { onRemoveCharacter(charConfig.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = SleekSlate500,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Presets selection row
                    Text(
                        text = "Voice Actor: ${HindiCharacterPresets.find { it.id == charConfig.voicePresetId }?.name ?: "Voice Preset"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SleekPurpleGlow
                    )

                    // Appearance consistency description
                    Text(
                        text = "Appearance Lock: ${charConfig.appearancePrompt}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = SleekSlate400
                    )
                }
            }
        }
    }
}

@Composable
private fun BgmAndSfxTab(
    selectedBgm: BackgroundMusicTrack,
    bgmVolume: Float,
    onSelectBgm: (BackgroundMusicTrack) -> Unit,
    onUpdateVolume: (Float) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "BACKGROUND SOUNDTRACK (BGM)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = SleekSlate400
            )
        }

        items(AvailableMusicTracks) { track ->
            val isSelected = selectedBgm.id == track.id
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) SleekPurplePrimary.copy(alpha = 0.15f) else SleekCard)
                    .border(
                        1.dp,
                        if (isSelected) SleekPurpleLight else SleekSlate800,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelectBgm(track) }
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${track.name} • ${track.tempo}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) SleekPurpleLight else SleekSlate200
                        )
                        Text(
                            text = "${track.genre} • Mood: ${track.mood}",
                            style = MaterialTheme.typography.labelSmall,
                            color = SleekSlate400
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = if (isSelected) SleekPurpleLight else SleekSlate500,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        item {
            // Volume slider
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SleekCard)
                    .border(1.dp, SleekSlate800, RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "BGM Volume Mix",
                        style = MaterialTheme.typography.labelSmall,
                        color = SleekSlate300
                    )
                    Text(
                        text = "${(bgmVolume * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = SleekPurpleLight
                    )
                }

                Slider(
                    value = bgmVolume,
                    onValueChange = onUpdateVolume,
                    valueRange = 0.0f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = SleekPurpleLight,
                        activeTrackColor = SleekPurplePrimary,
                        inactiveTrackColor = SleekSlate800
                    )
                )
            }
        }
    }
}
