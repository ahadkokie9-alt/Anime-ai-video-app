package com.example.backend.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Replicate API DTOs for Text-to-Video models.
 * Used for future Replicate API calls.
 */
@JsonClass(generateAdapter = true)
data class ReplicatePredictionRequest(
    @Json(name = "version")
    val version: String? = null,
    @Json(name = "input")
    val input: Map<String, Any>,
    @Json(name = "webhook")
    val webhook: String? = null
)

@JsonClass(generateAdapter = true)
data class ReplicatePredictionResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "model")
    val model: String? = null,
    @Json(name = "version")
    val version: String? = null,
    @Json(name = "status")
    val status: String, // "starting", "processing", "succeeded", "failed", "canceled"
    @Json(name = "input")
    val input: Map<String, Any>? = null,
    @Json(name = "output")
    val output: Any? = null, // Can be String URL or List<String>
    @Json(name = "error")
    val error: String? = null,
    @Json(name = "logs")
    val logs: String? = null,
    @Json(name = "metrics")
    val metrics: Map<String, Any>? = null,
    @Json(name = "created_at")
    val createdAt: String? = null,
    @Json(name = "started_at")
    val startedAt: String? = null,
    @Json(name = "completed_at")
    val completedAt: String? = null,
    @Json(name = "urls")
    val urls: ReplicateUrls? = null
)

@JsonClass(generateAdapter = true)
data class ReplicateUrls(
    @Json(name = "get")
    val get: String? = null,
    @Json(name = "cancel")
    val cancel: String? = null
)

/**
 * Supported anime video models catalog on Replicate
 */
data class AnimeVideoModelDescriptor(
    val id: String,
    val name: String,
    val replicateModelOwner: String,
    val replicateModelName: String,
    val defaultVersion: String,
    val description: String,
    val maxDurationSeconds: Int,
    val recommendedFps: Int,
    val isDefault: Boolean = false
)

val SupportedAnimeVideoModels = listOf(
    AnimeVideoModelDescriptor(
        id = "wan_2_1",
        name = "Wan 2.1 Anime Text-to-Video (14B)",
        replicateModelOwner = "wan-video",
        replicateModelName = "wan-2.1-t2v-14b",
        defaultVersion = "wan-2.1-t2v-14b-latest",
        description = "Next-gen state of the art open weights video generator with exceptional anime aesthetics, 9:16 native vertical support, and dynamic action rendering.",
        maxDurationSeconds = 60,
        recommendedFps = 24,
        isDefault = true
    ),
    AnimeVideoModelDescriptor(
        id = "cogvideox_5b",
        name = "CogVideoX-5B Anime Edition",
        replicateModelOwner = "thudm",
        replicateModelName = "cogvideox-5b",
        defaultVersion = "cogvideox-5b-v1",
        description = "High fidelity text-to-video model with cinematic camera control, smooth temporal consistency, and anime character motion.",
        maxDurationSeconds = 60,
        recommendedFps = 24
    ),
    AnimeVideoModelDescriptor(
        id = "animatediff_anime",
        name = "AnimateDiff Anime Matrix",
        replicateModelOwner = "lucataco",
        replicateModelName = "animate-diff",
        defaultVersion = "animatediff-anime-v3",
        description = "Specialized anime model with cel-shaded animation, expressive character acting, and custom LoRA support.",
        maxDurationSeconds = 30,
        recommendedFps = 16
    ),
    AnimeVideoModelDescriptor(
        id = "kling_v1",
        name = "Kling v1.5 High Quality",
        replicateModelOwner = "kwaivgi",
        replicateModelName = "kling-v1.5",
        defaultVersion = "kling-v1.5-standard",
        description = "Ultra realistic physical motion and 3D camera pan rendering for cinematic anime battle scenes.",
        maxDurationSeconds = 60,
        recommendedFps = 30
    )
)

/**
 * Hindi Voice Synthesis API models
 */
@JsonClass(generateAdapter = true)
data class HindiVoiceSynthesisRequest(
    @Json(name = "text")
    val text: String,
    @Json(name = "language_code")
    val languageCode: String = "hi-IN",
    @Json(name = "speaker_preset")
    val speakerPreset: String,
    @Json(name = "speaker_type")
    val speakerType: String, // "narrator" or "character"
    @Json(name = "pitch")
    val pitch: Float = 1.0f,
    @Json(name = "pace")
    val pace: Float = 1.0f,
    @Json(name = "emotional_tone")
    val emotionalTone: String = "dramatic"
)

@JsonClass(generateAdapter = true)
data class HindiVoiceSynthesisResponse(
    @Json(name = "audio_url")
    val audioUrl: String,
    @Json(name = "duration_seconds")
    val durationSeconds: Float,
    @Json(name = "format")
    val format: String = "mp3"
)
