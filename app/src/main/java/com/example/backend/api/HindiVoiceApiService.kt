package com.example.backend.api

import com.example.backend.model.HindiVoiceSynthesisRequest
import com.example.backend.model.HindiVoiceSynthesisResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Hindi Voice Synthesis & Audio Mastering API Client
 * Used for synthesizing separate Hindi narrator and character audio tracks.
 */
interface HindiVoiceApiService {

    @POST("v1/tts/hindi-synthesize")
    suspend fun synthesizeHindiSpeech(
        @Header("Authorization") authHeader: String,
        @Body request: HindiVoiceSynthesisRequest
    ): Response<HindiVoiceSynthesisResponse>
}
