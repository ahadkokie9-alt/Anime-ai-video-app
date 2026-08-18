package com.example.model

/**
 * Visual Style Options requested:
 * - 2D Anime
 * - 3D Anime Cartoon
 * - Cinematic Anime
 * - Cinematic 3D Anime
 */
enum class VisualAnimeStyle(
    val title: String,
    val subtitle: String,
    val promptTag: String,
    val details: String,
    val iconName: String
) {
    STYLE_2D_ANIME(
        title = "2D Anime",
        subtitle = "Classic Hand-Drawn & Cel Shaded",
        promptTag = "classic 2d anime aesthetic, sharp lineart, vibrant cel shading, studio anime production quality",
        details = "Classic Japanese animation style with expressive 2D outlines, rich character expressions, and hand-painted backgrounds.",
        iconName = "brush"
    ),
    STYLE_3D_ANIME_CARTOON(
        title = "3D Anime Cartoon",
        subtitle = "Stylized 3D Character CGI",
        promptTag = "stylized 3d anime cartoon render, smooth polygonal surfaces, vivid lighting, modern anime cgi",
        details = "Vibrant 3D animated look blending modern cartoon expressiveness with clean anime shading and dynamic geometry.",
        iconName = "view_in_ar"
    ),
    STYLE_CINEMATIC_ANIME(
        title = "Cinematic Anime",
        subtitle = "Hyper-Detailed Shinkai / Ufotable Aesthetic",
        promptTag = "masterpiece cinematic anime film, makoto shinkai aesthetic, volumetric lighting, god rays, particle sakura fx, depth of field, 8k anime art",
        details = "Breathtaking visual depth with photorealistic anime skies, glowing particle effects, atmospheric lighting, and high frame-rate motion.",
        iconName = "movie_filter"
    ),
    STYLE_CINEMATIC_3D_ANIME(
        title = "Cinematic 3D Anime",
        subtitle = "Next-Gen Photoreal Anime CGI",
        promptTag = "ultra-realistic cinematic 3d anime render, unreal engine 5 anime shaders, raytraced subsurface scattering, epic movie cinematography",
        details = "High-end cinematic CGI featuring realistic cloth physics, subsurface skin lighting, raytraced reflections, and blockbuster movie action.",
        iconName = "auto_awesome"
    )
}

enum class VideoAspectRatio(
    val label: String,
    val ratioValue: String,
    val width: Int,
    val height: Int,
    val description: String
) {
    PORTRAIT_9_16("9:16", "9:16", 1080, 1920, "Vertical Shorts & Reels (Recommended)"),
    LANDSCAPE_16_9("16:9", "16:9", 1920, 1080, "Cinematic Widescreen"),
    SQUARE_1_1("1:1", "1:1", 1080, 1080, "Square Social Feed")
}

data class CameraMovementOption(
    val id: String,
    val name: String,
    val description: String,
    val promptModifier: String
)

val AvailableCameraMovements = listOf(
    CameraMovementOption("dynamic_pan", "Dynamic Cinematic Pan", "Sweeping camera across scenic landscape", "smooth cinematic camera pan, establishing scale"),
    CameraMovementOption("orbit_track", "Orbit Subject Tracking", "360-degree circling around the main character", "360 orbit camera movement focusing on character action"),
    CameraMovementOption("dolly_zoom", "Dolly Zoom (Vertigo)", "Dramatic perspective warp for intense moments", "dolly zoom vertigo effect, dramatic tension"),
    CameraMovementOption("shonen_push", "Shonen Speed Push-in", "Rapid forward zoom with anime speed lines", "rapid anime camera push in with kinetic motion blur"),
    CameraMovementOption("dutch_tilt", "Dutch Angle Dynamic", "Stylized tilted angle for combat and suspense", "stylized dutch angle composition, dynamic framing")
)

data class AnimeLightingEffect(
    val id: String,
    val name: String,
    val description: String,
    val promptModifier: String
)

val AvailableAnimeEffects = listOf(
    AnimeLightingEffect("sakura_bloom", "Sakura Petal Bloom", "Floating cherry blossoms with soft glow", "floating sakura cherry blossom petals, ethereal glow"),
    AnimeLightingEffect("energy_aura", "Super Saiyan / Ki Aura", "Crackling energy flares & power aura", "crackling neon aura energy particles, electric sparks"),
    AnimeLightingEffect("god_rays", "Volumetric Sun Rays", "Atmospheric beam illumination", "volumetric god rays, atmospheric haze, dust motes"),
    AnimeLightingEffect("cyber_neon", "Neo-Tokyo Cyber Glow", "Vivid neon city reflections & rain", "neon cyberpunk reflections, wet asphalt, vivid cyan-magenta glow"),
    AnimeLightingEffect("speed_lines", "Action Speed Lines", "Kinetic impact lines for high velocity", "anime kinetic action lines, high speed streaks")
)

/**
 * Generation stages as requested:
 * 1. Preparing
 * 2. Generating scenes
 * 3. Generating Hindi voices
 * 4. Processing audio
 * 5. Combining scenes
 * 6. Finalizing video
 * 7. Complete
 */
enum class GenerationStage(
    val stageNumber: Int,
    val title: String,
    val description: String
) {
    IDLE(0, "Ready", "Configure parameters and start generation"),
    PREPARING(1, "Preparing", "Decomposing story into coherent sequential scenes and character prompts"),
    GENERATING_SCENES(2, "Generating scenes", "Synthesizing anime video clips with character appearance consistency"),
    GENERATING_HINDI_VOICES(3, "Generating Hindi voices", "Synthesizing Hindi narrator and separate character dialogue voice tracks"),
    PROCESSING_AUDIO(4, "Processing audio", "Mastering background score, sound effects, and dialogue ducking"),
    COMBINING_SCENES(5, "Combining scenes", "Stitching scene sequences with seamless cinematic transitions"),
    FINALIZING_VIDEO(6, "Finalizing video", "Encoding 9:16 high-definition anime video file and metadata"),
    COMPLETE(7, "Complete", "Anime video generated and ready for preview & download"),
    FAILED(0, "Pipeline Stopped", "Generation encountered an issue or requires API provider configuration")
}

data class HindiVoicePreset(
    val id: String,
    val name: String,
    val gender: String,
    val tone: String,
    val sampleTextHindi: String,
    val sampleTextEnglish: String
)

val HindiNarratorPresets = listOf(
    HindiVoicePreset(
        id = "narrator_dramatic",
        name = "Kabir (Dramatic Anime Narrator)",
        gender = "Male",
        tone = "Deep, Epic & Cinematic",
        sampleTextHindi = "अंधेरे की गहराइयों में, एक नई ताक़त का जन्म हो रहा था...",
        sampleTextEnglish = "In the depths of darkness, a new power was awakening..."
    ),
    HindiVoicePreset(
        id = "narrator_hype",
        name = "Arjun (Shonen Hype Narrator)",
        gender = "Male",
        tone = "Energetic, Fast-Paced & Thrilling",
        sampleTextHindi = "अब शुरू होता है इस सदी का सबसे बड़ा महायुद्ध!",
        sampleTextEnglish = "Now begins the greatest battle of this century!"
    ),
    HindiVoicePreset(
        id = "narrator_sage",
        name = "Guruji (Calm Epic Sage)",
        gender = "Male",
        tone = "Wise, Mystical & Resonant",
        sampleTextHindi = "नियति का पहिया घूम चुका है, समय किसी का इंतज़ार नहीं करता।",
        sampleTextEnglish = "The wheel of destiny has turned, time waits for no one."
    ),
    HindiVoicePreset(
        id = "narrator_gentle",
        name = "Meera (Soft Anime Storyteller)",
        gender = "Female",
        tone = "Emotional, Gentle & Expressive",
        sampleTextHindi = "उन यादों की खुशबू आज भी उस शाम की हवा में थी...",
        sampleTextEnglish = "The scent of those memories still lingered in the evening breeze..."
    )
)

val HindiCharacterPresets = listOf(
    HindiVoicePreset(
        id = "char_rohan_hero",
        name = "Rohan (Youthful Shonen Protagonist)",
        gender = "Male",
        tone = "Passionate, Brave & Determined",
        sampleTextHindi = "मैं कभी हार नहीं मानूँगा, चाहे जो हो जाए!",
        sampleTextEnglish = "I will never give up, no matter what happens!"
    ),
    HindiVoicePreset(
        id = "char_priya_heroine",
        name = "Priya (Fierce Anime Heroine)",
        gender = "Female",
        tone = "Confident, Sharp & Compassionate",
        sampleTextHindi = "अपनी तलवार उठाओ! हम एक साथ लड़ेंगे!",
        sampleTextEnglish = "Raise your blade! We will fight together!"
    ),
    HindiVoicePreset(
        id = "char_vikram_rival",
        name = "Vikram (Dark Antihero / Rival)",
        gender = "Male",
        tone = "Cold, Calculating & Powerful",
        sampleTextHindi = "तुम अभी भी मेरी असली ताक़त से अनजान हो।",
        sampleTextEnglish = "You are still unaware of my true power."
    ),
    HindiVoicePreset(
        id = "char_ananya_mage",
        name = "Ananya (Mystic Mage)",
        gender = "Female",
        tone = "Soft-Spoken, Ethereal & Mysterious",
        sampleTextHindi = "हवा के तत्व... मेरी पुकार सुनो और रक्षा करो!",
        sampleTextEnglish = "Elements of the wind... hear my call and protect us!"
    ),
    HindiVoicePreset(
        id = "char_deva_villain",
        name = "Devraj (Dark Overlord)",
        gender = "Male",
        tone = "Deep Menacing Baritone",
        sampleTextHindi = "यह दुनिया सिर्फ़ ताक़त की भाषा समझती है!",
        sampleTextEnglish = "This world only understands the language of power!"
    )
)

data class HindiCharacterVoiceConfig(
    val id: String,
    val characterName: String,
    val role: String,
    val voicePresetId: String,
    val pitch: Float = 1.0f,
    val speed: Float = 1.0f,
    val volume: Float = 0.9f,
    val appearancePrompt: String = "matching character design, consistent hairstyle and costume colors"
)

data class BackgroundMusicTrack(
    val id: String,
    val name: String,
    val genre: String,
    val tempo: String,
    val mood: String
)

val AvailableMusicTracks = listOf(
    BackgroundMusicTrack("bgm_epic_battle", "Thunder & Katana (Epic Shonen Battle)", "Orchestral Rock", "145 BPM", "High Adrenaline, Heroic Climax"),
    BackgroundMusicTrack("bgm_emotional_piano", "Sakura Tears (Emotional Strings & Piano)", "Orchestral Ballad", "78 BPM", "Heartfelt, Melancholic, Cinematic"),
    BackgroundMusicTrack("bgm_cyber_synth", "Neo-Shinjuku Drive (Cyberpunk Synthwave)", "Synthwave / Darksynth", "128 BPM", "Futuristic, Neon, Fast Pace"),
    BackgroundMusicTrack("bgm_traditional_taiko", "Ronin Spirit (Traditional Taiko & Shamisen)", "Japanese Folk Fusion", "110 BPM", "Mystical, Samurai Lore, Dramatic"),
    BackgroundMusicTrack("bgm_shonen_nostalgia", "Breeze of Youth (Lofi Anime Nostalgia)", "Chill Anime Lofi", "85 BPM", "Calm, School Life, Peaceful")
)

data class AnimeSoundEffectItem(
    val id: String,
    val name: String,
    val category: String,
    val enabled: Boolean = true
)

val DefaultSoundEffects = listOf(
    AnimeSoundEffectItem("sfx_katana_slash", "Katana Energy Slash", "Combat"),
    AnimeSoundEffectItem("sfx_energy_blast", "Kamehameha / Energy Blast", "Combat"),
    AnimeSoundEffectItem("sfx_whoosh", "Kinetic Speed Dash & Whoosh", "Motion"),
    AnimeSoundEffectItem("sfx_magic_sparkle", "Ethereal Magic Chime", "Fantasy"),
    AnimeSoundEffectItem("sfx_thunder", "Cinematic Thunder Roar", "Atmosphere"),
    AnimeSoundEffectItem("sfx_rain", "Tokyo Rain & Urban Ambience", "Atmosphere")
)

data class StorySceneSpec(
    val sceneNumber: Int,
    val title: String,
    val durationSeconds: Int,
    val visualPrompt: String,
    val cameraMovement: String,
    val narratorHindiDialogue: String,
    val characterDialogue: String,
    val transition: String = "Crossfade",
    val sfxTrigger: String = "Katana Slash"
)
