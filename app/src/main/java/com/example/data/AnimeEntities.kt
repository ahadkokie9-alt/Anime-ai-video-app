package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_projects")
data class AnimeProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val storyPrompt: String,
    val visualStyle: String,
    val durationSeconds: Int = 60,
    val aspectRatio: String = "9:16",
    val characterConsistency: Boolean = true,
    val sceneContinuity: Boolean = true,
    val cameraMovement: String = "dynamic_pan",
    val lightingEffect: String = "sakura_bloom",
    val smoothTransitions: Boolean = true,
    val narratorEnabled: Boolean = true,
    val narratorVoiceId: String = "narrator_dramatic",
    val narratorPitch: Float = 1.0f,
    val narratorSpeed: Float = 1.0f,
    val bgmTrackId: String = "bgm_epic_battle",
    val bgmVolume: Float = 0.5f,
    val sfxEnabled: Boolean = true,
    val status: String = "IDLE", // IDLE, PREPARING, GENERATING_SCENES, etc.
    val progressPercent: Int = 0,
    val statusMessage: String = "Ready to generate",
    val videoPreviewUrl: String? = null,
    val audioMasterUrl: String? = null,
    val replicatePredictionId: String? = null,
    val totalScenesCount: Int = 4,
    val createdAtTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "anime_scenes")
data class AnimeSceneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val sceneIndex: Int,
    val sceneTitle: String,
    val durationSeconds: Int,
    val visualPrompt: String,
    val cameraMotion: String,
    val narratorDialogueHindi: String,
    val characterDialogueHindi: String,
    val characterSpeakerName: String,
    val transitionType: String,
    val sfxCue: String,
    val videoClipUrl: String? = null,
    val audioClipUrl: String? = null,
    val isGenerated: Boolean = false
)

@Entity(tableName = "anime_character_voices")
data class AnimeCharacterVoiceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val characterName: String,
    val role: String,
    val voicePresetId: String,
    val pitch: Float = 1.0f,
    val speed: Float = 1.0f,
    val volume: Float = 0.9f,
    val appearanceDetails: String = ""
)
