package com.example.backend.security

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig

/**
 * Manages resolution of AI provider secrets securely.
 * Priority:
 * 1. BuildConfig (injected at build time from .env / Secrets panel)
 * 2. Secure local configuration for testing/development
 */
class SecretKeyResolver(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "anime_video_secure_secrets",
        Context.MODE_PRIVATE
    )

    fun getReplicateApiToken(): String? {
        // First check BuildConfig injected from .env via secrets gradle plugin
        val buildConfigKey = try {
            val field = BuildConfig::class.java.getField("REPLICATE_API_TOKEN")
            field.get(null) as? String
        } catch (e: Exception) {
            null
        }

        if (!buildConfigKey.isNullOrBlank() && buildConfigKey != "r8_your_replicate_token_here") {
            return buildConfigKey
        }

        // Check local secure storage
        val localKey = prefs.getString(KEY_REPLICATE_TOKEN, null)
        if (!localKey.isNullOrBlank()) {
            return localKey
        }

        return null
    }

    fun saveReplicateApiToken(token: String) {
        prefs.edit().putString(KEY_REPLICATE_TOKEN, token.trim()).apply()
    }

    fun getHindiVoiceApiKey(): String? {
        val buildConfigKey = try {
            val field = BuildConfig::class.java.getField("HINDI_VOICE_API_KEY")
            field.get(null) as? String
        } catch (e: Exception) {
            null
        }

        if (!buildConfigKey.isNullOrBlank() && buildConfigKey != "your_voice_api_key_here") {
            return buildConfigKey
        }

        return prefs.getString(KEY_HINDI_VOICE_KEY, null)
    }

    fun saveHindiVoiceApiKey(key: String) {
        prefs.edit().putString(KEY_HINDI_VOICE_KEY, key.trim()).apply()
    }

    fun hasReplicateKey(): Boolean {
        return !getReplicateApiToken().isNullOrBlank()
    }

    fun clearKeys() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_REPLICATE_TOKEN = "sec_replicate_api_token"
        private const val KEY_HINDI_VOICE_KEY = "sec_hindi_voice_api_key"
    }
}
