package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.backend.pipeline.AnimeVideoPipelineManager
import com.example.backend.pipeline.PipelineState
import com.example.backend.provider.AudioMixerProviderImpl
import com.example.backend.provider.HindiVoiceProviderImpl
import com.example.backend.provider.ReplicateVideoProvider
import com.example.backend.provider.SceneDecomposerProviderImpl
import com.example.backend.provider.SceneStitcherProviderImpl
import com.example.backend.security.SecretKeyResolver
import com.example.data.AnimeCharacterVoiceEntity
import com.example.data.AnimeProjectDao
import com.example.data.AnimeProjectEntity
import com.example.data.AnimeSceneEntity
import com.example.data.AppDatabase
import com.example.model.AnimeLightingEffect
import com.example.model.AnimeSoundEffectItem
import com.example.model.AvailableAnimeEffects
import com.example.model.AvailableCameraMovements
import com.example.model.AvailableMusicTracks
import com.example.model.BackgroundMusicTrack
import com.example.model.CameraMovementOption
import com.example.model.DefaultSoundEffects
import com.example.model.GenerationStage
import com.example.model.HindiCharacterPresets
import com.example.model.HindiCharacterVoiceConfig
import com.example.model.HindiNarratorPresets
import com.example.model.HindiVoicePreset
import com.example.model.StorySceneSpec
import com.example.model.VideoAspectRatio
import com.example.model.VisualAnimeStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppNavigationTab(val title: String, val iconName: String) {
    STUDIO("Create Studio", "auto_fix_high"),
    VOICE_AUDIO("Hindi Voices", "record_voice_over"),
    STORYBOARD("Storyboards", "movie_filter"),
    PROJECTS("Gallery", "video_library"),
    API_BACKEND("Backend API", "cloud_sync")
}

data class AnimePromptTemplate(
    val title: String,
    val genre: String,
    val prompt: String,
    val recommendedStyle: VisualAnimeStyle
)

val SampleAnimePrompts = listOf(
    AnimePromptTemplate(
        title = "Cyber-Shinjuku Katana Duel",
        genre = "Cyberpunk / Shonen",
        prompt = "In the neon-drenched rainy alleys of Cyber-Shinjuku 2088, young ronin swordsman Ren with glowing cyan katana confronts cyborg shadow warlord Kuro in a high-speed clash of lightning sparks and holographic sakura blossoms.",
        recommendedStyle = VisualAnimeStyle.STYLE_CINEMATIC_ANIME
    ),
    AnimePromptTemplate(
        title = "Celestial Dragon Awakening",
        genre = "High Fantasy / Action",
        prompt = "Atop the floating crystal spires of Mount Tenrai, teenage fire mage Aarav unlocks the legendary Golden Dragon Spirit to defend his village against an invading armada of dark shadow beasts with blazing anime particle effects.",
        recommendedStyle = VisualAnimeStyle.STYLE_CINEMATIC_3D_ANIME
    ),
    AnimePromptTemplate(
        title = "High School Shonen Tournament",
        genre = "Shonen Battle / Sports",
        prompt = "During the grand finals of the Spirit Martial Arts Tournament in a packed futuristic stadium, hot-blooded fighter Rohan unleashes his ultimate kinetic combo against fierce prodigy Maya under stadium floodlights.",
        recommendedStyle = VisualAnimeStyle.STYLE_2D_ANIME
    ),
    AnimePromptTemplate(
        title = "Isekai Kingdom Mech Odyssey",
        genre = "Sci-Fi / Mecha",
        prompt = "A colossal chrome anime mecha unit powered by ancient arcane runes descends from the storm clouds into a lush fantasy valley, guided by pilot Ananya with glowing violet visor HUD.",
        recommendedStyle = VisualAnimeStyle.STYLE_3D_ANIME_CARTOON
    )
)

data class StudioFormState(
    val storyPrompt: String = "In the neon-drenched rainy alleys of Cyber-Shinjuku 2088, young swordsman Rohan with a glowing electric katana confronts the dark shadow warlord Vikram in an epic clash of energy blasts and sakura blossoms.",
    val selectedStyle: VisualAnimeStyle = VisualAnimeStyle.STYLE_CINEMATIC_ANIME,
    val durationSeconds: Int = 60,
    val selectedAspectRatio: VideoAspectRatio = VideoAspectRatio.PORTRAIT_9_16,
    val characterConsistencyEnabled: Boolean = true,
    val sceneContinuityEnabled: Boolean = true,
    val selectedCameraMovement: CameraMovementOption = AvailableCameraMovements[0],
    val selectedLightingEffect: AnimeLightingEffect = AvailableAnimeEffects[0],
    val smoothTransitionsEnabled: Boolean = true,
    // Voice settings
    val narratorEnabled: Boolean = true,
    val selectedNarratorPreset: HindiVoicePreset = HindiNarratorPresets[0],
    val narratorPitch: Float = 1.0f,
    val narratorSpeed: Float = 1.0f,
    val narratorSampleScript: String = "अंधेरे की गहराइयों में, एक नई ताक़त का जन्म हो रहा था...",
    // Character voices
    val characterVoices: List<HindiCharacterVoiceConfig> = listOf(
        HindiCharacterVoiceConfig(
            id = "char_1",
            characterName = "Rohan (Hero)",
            role = "Protagonist",
            voicePresetId = HindiCharacterPresets[0].id,
            pitch = 1.0f,
            speed = 1.0f,
            appearancePrompt = "Spiky black anime hair with cyan streaks, dark cyberpunk trenchcoat with glowing trim"
        ),
        HindiCharacterVoiceConfig(
            id = "char_2",
            characterName = "Vikram (Rival)",
            role = "Rival / Antihero",
            voicePresetId = HindiCharacterPresets[2].id,
            pitch = 0.9f,
            speed = 1.05f,
            appearancePrompt = "Silver hair, crimson eye glow, obsidian tactical armor with violet energy crest"
        )
    ),
    // Audio settings
    val selectedBgmTrack: BackgroundMusicTrack = AvailableMusicTracks[0],
    val bgmVolume: Float = 0.5f,
    val soundEffects: List<AnimeSoundEffectItem> = DefaultSoundEffects,
    // Secret management
    val customReplicateTokenInput: String = "",
    val customHindiVoiceKeyInput: String = "",
    val isSecretTokenConfigured: Boolean = false,
    val isVoiceKeyConfigured: Boolean = false
)

class AnimeVideoViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val projectDao: AnimeProjectDao = db.animeProjectDao()
    val secretKeyResolver = SecretKeyResolver(application)

    private val videoProvider = ReplicateVideoProvider(secretKeyResolver)
    private val hindiVoiceProvider = HindiVoiceProviderImpl(secretKeyResolver)
    private val sceneDecomposer = SceneDecomposerProviderImpl()
    private val audioMixer = AudioMixerProviderImpl()
    private val sceneStitcher = SceneStitcherProviderImpl()

    private val pipelineManager = AnimeVideoPipelineManager(
        projectDao = projectDao,
        videoProvider = videoProvider,
        hindiVoiceProvider = hindiVoiceProvider,
        sceneDecomposer = sceneDecomposer,
        audioMixer = audioMixer,
        sceneStitcher = sceneStitcher,
        scope = viewModelScope
    )

    val pipelineState: StateFlow<PipelineState> = pipelineManager.pipelineState

    private val _selectedTab = MutableStateFlow(AppNavigationTab.STUDIO)
    val selectedTab: StateFlow<AppNavigationTab> = _selectedTab.asStateFlow()

    private val _formState = MutableStateFlow(StudioFormState())
    val formState: StateFlow<StudioFormState> = _formState.asStateFlow()

    val allProjects: StateFlow<List<AnimeProjectEntity>> = projectDao.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedProjectId = MutableStateFlow<Long?>(null)
    val selectedProjectId: StateFlow<Long?> = _selectedProjectId.asStateFlow()

    private val _selectedProjectScenes = MutableStateFlow<List<AnimeSceneEntity>>(emptyList())
    val selectedProjectScenes: StateFlow<List<AnimeSceneEntity>> = _selectedProjectScenes.asStateFlow()

    // Video Player State
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPlayheadSeconds = MutableStateFlow(0f)
    val currentPlayheadSeconds: StateFlow<Float> = _currentPlayheadSeconds.asStateFlow()

    init {
        refreshSecretStatus()
    }

    fun refreshSecretStatus() {
        val hasReplicate = secretKeyResolver.hasReplicateKey()
        val hasVoice = !secretKeyResolver.getHindiVoiceApiKey().isNullOrBlank()
        _formState.value = _formState.value.copy(
            isSecretTokenConfigured = hasReplicate,
            isVoiceKeyConfigured = hasVoice
        )
    }

    fun selectTab(tab: AppNavigationTab) {
        _selectedTab.value = tab
    }

    fun updateStoryPrompt(text: String) {
        _formState.value = _formState.value.copy(storyPrompt = text)
    }

    fun applyPromptTemplate(template: AnimePromptTemplate) {
        _formState.value = _formState.value.copy(
            storyPrompt = template.prompt,
            selectedStyle = template.recommendedStyle
        )
    }

    fun selectVisualStyle(style: VisualAnimeStyle) {
        _formState.value = _formState.value.copy(selectedStyle = style)
    }

    fun selectDuration(seconds: Int) {
        _formState.value = _formState.value.copy(durationSeconds = seconds)
    }

    fun selectAspectRatio(ratio: VideoAspectRatio) {
        _formState.value = _formState.value.copy(selectedAspectRatio = ratio)
    }

    fun toggleCharacterConsistency(enabled: Boolean) {
        _formState.value = _formState.value.copy(characterConsistencyEnabled = enabled)
    }

    fun toggleSceneContinuity(enabled: Boolean) {
        _formState.value = _formState.value.copy(sceneContinuityEnabled = enabled)
    }

    fun selectCameraMovement(movement: CameraMovementOption) {
        _formState.value = _formState.value.copy(selectedCameraMovement = movement)
    }

    fun selectLightingEffect(effect: AnimeLightingEffect) {
        _formState.value = _formState.value.copy(selectedLightingEffect = effect)
    }

    fun toggleSmoothTransitions(enabled: Boolean) {
        _formState.value = _formState.value.copy(smoothTransitionsEnabled = enabled)
    }

    // Voice & Audio
    fun toggleNarrator(enabled: Boolean) {
        _formState.value = _formState.value.copy(narratorEnabled = enabled)
    }

    fun selectNarratorPreset(preset: HindiVoicePreset) {
        _formState.value = _formState.value.copy(
            selectedNarratorPreset = preset,
            narratorSampleScript = preset.sampleTextHindi
        )
    }

    fun updateNarratorPitch(pitch: Float) {
        _formState.value = _formState.value.copy(narratorPitch = pitch)
    }

    fun updateNarratorSpeed(speed: Float) {
        _formState.value = _formState.value.copy(narratorSpeed = speed)
    }

    fun updateNarratorSampleScript(script: String) {
        _formState.value = _formState.value.copy(narratorSampleScript = script)
    }

    // Character Voice Actions
    fun addCharacterVoice() {
        val current = _formState.value.characterVoices.toMutableList()
        val index = current.size + 1
        val preset = HindiCharacterPresets.getOrElse(index % HindiCharacterPresets.size) { HindiCharacterPresets.last() }
        current.add(
            HindiCharacterVoiceConfig(
                id = "char_${UUID.randomUUID().toString().take(6)}",
                characterName = "Character $index",
                role = if (index == 3) "Support Mage" else "Ally",
                voicePresetId = preset.id,
                pitch = 1.0f,
                speed = 1.0f,
                appearancePrompt = "Distinct anime design with matching team colors"
            )
        )
        _formState.value = _formState.value.copy(characterVoices = current)
    }

    fun removeCharacterVoice(id: String) {
        val current = _formState.value.characterVoices.filterNot { it.id == id }
        _formState.value = _formState.value.copy(characterVoices = current)
    }

    fun updateCharacterVoice(updated: HindiCharacterVoiceConfig) {
        val current = _formState.value.characterVoices.map {
            if (it.id == updated.id) updated else it
        }
        _formState.value = _formState.value.copy(characterVoices = current)
    }

    // Audio & SFX
    fun selectBgmTrack(track: BackgroundMusicTrack) {
        _formState.value = _formState.value.copy(selectedBgmTrack = track)
    }

    fun updateBgmVolume(volume: Float) {
        _formState.value = _formState.value.copy(bgmVolume = volume)
    }

    fun toggleSfxItem(id: String, enabled: Boolean) {
        val current = _formState.value.soundEffects.map {
            if (it.id == id) it.copy(enabled = enabled) else it
        }
        _formState.value = _formState.value.copy(soundEffects = current)
    }

    // Secrets configuration
    fun updateCustomReplicateToken(token: String) {
        _formState.value = _formState.value.copy(customReplicateTokenInput = token)
    }

    fun updateCustomHindiVoiceKey(key: String) {
        _formState.value = _formState.value.copy(customHindiVoiceKeyInput = key)
    }

    fun saveReplicateToken() {
        val token = _formState.value.customReplicateTokenInput.trim()
        if (token.isNotEmpty()) {
            secretKeyResolver.saveReplicateApiToken(token)
            refreshSecretStatus()
            _formState.value = _formState.value.copy(customReplicateTokenInput = "")
        }
    }

    fun saveHindiVoiceKey() {
        val key = _formState.value.customHindiVoiceKeyInput.trim()
        if (key.isNotEmpty()) {
            secretKeyResolver.saveHindiVoiceApiKey(key)
            refreshSecretStatus()
            _formState.value = _formState.value.copy(customHindiVoiceKeyInput = "")
        }
    }

    fun clearStoredSecrets() {
        secretKeyResolver.clearKeys()
        refreshSecretStatus()
    }

    // Generation Action
    fun startVideoGeneration() {
        val form = _formState.value
        val title = form.storyPrompt.take(40).ifBlank { "Anime Epic Video" }

        val project = AnimeProjectEntity(
            title = title,
            storyPrompt = form.storyPrompt,
            visualStyle = form.selectedStyle.title,
            durationSeconds = form.durationSeconds,
            aspectRatio = form.selectedAspectRatio.ratioValue,
            characterConsistency = form.characterConsistencyEnabled,
            sceneContinuity = form.sceneContinuityEnabled,
            cameraMovement = form.selectedCameraMovement.id,
            lightingEffect = form.selectedLightingEffect.id,
            smoothTransitions = form.smoothTransitionsEnabled,
            narratorEnabled = form.narratorEnabled,
            narratorVoiceId = form.selectedNarratorPreset.id,
            narratorPitch = form.narratorPitch,
            narratorSpeed = form.narratorSpeed,
            bgmTrackId = form.selectedBgmTrack.id,
            bgmVolume = form.bgmVolume,
            sfxEnabled = form.soundEffects.any { it.enabled },
            status = GenerationStage.PREPARING.name,
            progressPercent = 0,
            statusMessage = "Starting anime video pipeline..."
        )

        pipelineManager.startGeneration(
            projectEntity = project,
            characterVoices = form.characterVoices,
            visualStyle = form.selectedStyle
        )
    }

    fun cancelVideoGeneration() {
        pipelineManager.cancelGeneration()
    }

    fun selectProject(projectId: Long) {
        _selectedProjectId.value = projectId
        viewModelScope.launch {
            val scenes = projectDao.getScenesForProjectDirect(projectId)
            _selectedProjectScenes.value = scenes
        }
    }

    fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            projectDao.deleteProject(projectId)
            projectDao.deleteScenesForProject(projectId)
            projectDao.deleteCharacterVoicesForProject(projectId)
            if (_selectedProjectId.value == projectId) {
                _selectedProjectId.value = null
                _selectedProjectScenes.value = emptyList()
            }
        }
    }

    fun togglePlayback() {
        _isPlaying.value = !_isPlaying.value
    }

    fun seekPlayhead(seconds: Float) {
        _currentPlayheadSeconds.value = seconds.coerceIn(0f, _formState.value.durationSeconds.toFloat())
    }
}
