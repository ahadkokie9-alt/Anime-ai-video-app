package com.example.backend.provider

import com.example.model.HindiCharacterVoiceConfig
import com.example.model.StorySceneSpec
import com.example.model.VisualAnimeStyle

/**
 * Common request payload for video generation
 */
data class VideoGenerationJobRequest(
    val storyPrompt: String,
    val visualStyle: VisualAnimeStyle,
    val durationSeconds: Int = 60,
    val aspectRatio: String = "9:16",
    val characterConsistency: Boolean = true,
    val sceneContinuity: Boolean = true,
    val cameraMovement: String = "dynamic_pan",
    val lightingEffect: String = "sakura_bloom",
    val smoothTransitions: Boolean = true,
    val selectedModelId: String = "wan_2_1"
)

sealed class ProviderConfigStatus {
    object Ready : ProviderConfigStatus()
    data class Unconfigured(
        val providerName: String,
        val missingKeyName: String,
        val instructions: String
    ) : ProviderConfigStatus()
}

sealed class VideoJobResult {
    data class Success(val predictionId: String, val status: String, val videoUrl: String? = null) : VideoJobResult()
    data class InProgress(val predictionId: String, val progressPercent: Int, val logMessage: String) : VideoJobResult()
    data class Failed(val reason: String, val isConfigError: Boolean = false) : VideoJobResult()
}

data class SceneGenerationResult(
    val sceneIndex: Int,
    val clipUrl: String,
    val durationSeconds: Int,
    val promptUsed: String
)

data class HindiVoiceJobResult(
    val audioUrl: String,
    val speakerType: String,
    val durationSeconds: Float,
    val textSpoken: String
)

/**
 * Text-to-Video AI Provider Interface
 * Allows plugging in Replicate or any other provider seamlessly without UI changes.
 */
interface VideoGenerationProvider {
    fun getProviderName(): String
    fun checkConfiguration(): ProviderConfigStatus
    suspend fun createVideoPrediction(request: VideoGenerationJobRequest): VideoJobResult
    suspend fun pollPredictionStatus(predictionId: String): VideoJobResult
    suspend fun cancelPrediction(predictionId: String): Boolean
}

/**
 * Hindi Voice Synthesis Provider Interface
 * Separates narrator voice generation and individual character voices.
 */
interface HindiVoiceSynthesisProvider {
    fun getProviderName(): String
    fun checkConfiguration(): ProviderConfigStatus
    suspend fun synthesizeNarrator(textHindi: String, voicePresetId: String, pitch: Float, speed: Float): HindiVoiceJobResult
    suspend fun synthesizeCharacter(character: HindiCharacterVoiceConfig, dialogueHindi: String): HindiVoiceJobResult
}

/**
 * Scene Decomposer Provider Interface
 * Breaks down a 60-second anime story into sequential storyboard scenes with character locks.
 */
interface SceneDecomposerProvider {
    suspend fun decomposeStory(
        storyPrompt: String,
        visualStyle: VisualAnimeStyle,
        durationSeconds: Int,
        cameraMovement: String,
        lightingEffect: String,
        characterConsistency: Boolean,
        characters: List<HindiCharacterVoiceConfig>
    ): List<StorySceneSpec>
}

/**
 * Audio Mixer Provider Interface
 * Combines Hindi dialogue, background music, and anime sound effects.
 */
interface AudioMixerProvider {
    suspend fun mixAudioTracks(
        narratorAudioUrls: List<String>,
        characterAudioUrls: List<String>,
        bgmTrackId: String,
        bgmVolume: Float,
        sfxList: List<String>
    ): String
}

/**
 * Scene Stitcher Provider Interface
 * Combines scene video clips with smooth anime transitions.
 */
interface SceneStitcherProvider {
    suspend fun combineScenes(
        sceneClips: List<SceneGenerationResult>,
        audioTrackUrl: String,
        transitionType: String
    ): String
}
