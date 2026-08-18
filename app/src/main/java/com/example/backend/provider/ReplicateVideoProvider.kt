package com.example.backend.provider

import com.example.backend.api.ApiClientFactory
import com.example.backend.model.ReplicatePredictionRequest
import com.example.backend.model.SupportedAnimeVideoModels
import com.example.backend.security.SecretKeyResolver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReplicateVideoProvider(
    private val secretKeyResolver: SecretKeyResolver
) : VideoGenerationProvider {

    override fun getProviderName(): String = "Replicate Video AI (Wan 2.1 / CogVideoX / AnimateDiff)"

    override fun checkConfiguration(): ProviderConfigStatus {
        val token = secretKeyResolver.getReplicateApiToken()
        return if (token.isNullOrBlank()) {
            ProviderConfigStatus.Unconfigured(
                providerName = "Replicate",
                missingKeyName = "REPLICATE_API_TOKEN",
                instructions = "Add your Replicate API token to enable cloud text-to-video rendering. You can configure this in the Secrets panel or via REPLICATE_API_TOKEN in your .env file."
            )
        } else {
            ProviderConfigStatus.Ready
        }
    }

    override suspend fun createVideoPrediction(request: VideoGenerationJobRequest): VideoJobResult = withContext(Dispatchers.IO) {
        val token = secretKeyResolver.getReplicateApiToken()
        if (token.isNullOrBlank()) {
            return@withContext VideoJobResult.Failed(
                reason = "Replicate API token is not configured. Backend architecture is prepared. To start real generation, add REPLICATE_API_TOKEN in Secrets panel or .env.",
                isConfigError = true
            )
        }

        try {
            // Build the enhanced anime prompt with character consistency & lighting
            val enhancedPrompt = buildAnimePrompt(request)
            val modelDescriptor = SupportedAnimeVideoModels.find { it.id == request.selectedModelId }
                ?: SupportedAnimeVideoModels.first()

            val inputPayload = mutableMapOf<String, Any>(
                "prompt" to enhancedPrompt,
                "negative_prompt" to "blurry, low quality, deformed anatomy, extra limbs, bad eyes, jitter, flicker, low resolution, realistic photo, western comic",
                "num_frames" to 81,
                "fps" to modelDescriptor.recommendedFps,
                "aspect_ratio" to request.aspectRatio,
                "guidance_scale" to 7.5
            )

            val apiRequest = ReplicatePredictionRequest(
                version = modelDescriptor.defaultVersion,
                input = inputPayload
            )

            val authHeader = "Bearer $token"
            val response = ApiClientFactory.replicateApi.createPrediction(
                authHeader = authHeader,
                request = apiRequest
            )

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                VideoJobResult.Success(
                    predictionId = body.id,
                    status = body.status,
                    videoUrl = parseOutputUrl(body.output)
                )
            } else {
                val errorMsg = response.errorBody()?.string() ?: "HTTP ${response.code()}: ${response.message()}"
                VideoJobResult.Failed("Replicate API error: $errorMsg")
            }
        } catch (e: Exception) {
            VideoJobResult.Failed("Network or provider connection failure: ${e.localizedMessage ?: e.message}")
        }
    }

    override suspend fun pollPredictionStatus(predictionId: String): VideoJobResult = withContext(Dispatchers.IO) {
        val token = secretKeyResolver.getReplicateApiToken()
        if (token.isNullOrBlank()) {
            return@withContext VideoJobResult.Failed(
                reason = "Replicate API token not found during status polling.",
                isConfigError = true
            )
        }

        try {
            val response = ApiClientFactory.replicateApi.getPredictionStatus(
                authHeader = "Bearer $token",
                predictionId = predictionId
            )

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                when (body.status.lowercase()) {
                    "succeeded" -> {
                        val videoUrl = parseOutputUrl(body.output)
                        VideoJobResult.Success(
                            predictionId = body.id,
                            status = "succeeded",
                            videoUrl = videoUrl
                        )
                    }
                    "starting" -> {
                        VideoJobResult.InProgress(body.id, 15, "Model starting on GPU cluster...")
                    }
                    "processing" -> {
                        VideoJobResult.InProgress(body.id, 50, "Rendering frames with anime shaders...")
                    }
                    "failed" -> {
                        VideoJobResult.Failed("Prediction failed: ${body.error ?: "Unknown error"}")
                    }
                    "canceled" -> {
                        VideoJobResult.Failed("Prediction was canceled.")
                    }
                    else -> {
                        VideoJobResult.InProgress(body.id, 30, "Status: ${body.status}")
                    }
                }
            } else {
                VideoJobResult.Failed("Polling status error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            VideoJobResult.Failed("Failed to poll Replicate status: ${e.localizedMessage ?: e.message}")
        }
    }

    override suspend fun cancelPrediction(predictionId: String): Boolean = withContext(Dispatchers.IO) {
        val token = secretKeyResolver.getReplicateApiToken() ?: return@withContext false
        try {
            val response = ApiClientFactory.replicateApi.cancelPrediction(
                authHeader = "Bearer $token",
                predictionId = predictionId
            )
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    private fun buildAnimePrompt(request: VideoGenerationJobRequest): String {
        val styleTag = request.visualStyle.promptTag
        val consistencyTag = if (request.characterConsistency) {
            "locked character appearance, exact same anime hairstyle, consistent costume design and outfit colors across cuts, distinct facial features"
        } else ""
        val continuityTag = if (request.sceneContinuity) {
            "sequential anime narrative continuity, cinematic camera tracking, fluid motion transitions"
        } else ""

        return "${request.storyPrompt}, $styleTag, $consistencyTag, $continuityTag, aspect ratio ${request.aspectRatio}, duration ${request.durationSeconds}s, high quality anime production"
    }

    private fun parseOutputUrl(output: Any?): String? {
        if (output == null) return null
        return when (output) {
            is String -> output
            is List<*> -> output.firstOrNull()?.toString()
            else -> output.toString()
        }
    }
}
