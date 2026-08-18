package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.BackendApiScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.StoryboardScreen
import com.example.ui.screens.StudioHomeScreen
import com.example.ui.theme.AnimeVideoAITheme
import com.example.ui.theme.SleekBackground
import com.example.ui.theme.SleekPurpleLight
import com.example.ui.theme.SleekPurplePrimary
import com.example.ui.theme.SleekSlate400
import com.example.ui.theme.SleekSlate500
import com.example.ui.theme.SleekSlate800
import com.example.ui.theme.SleekSurface
import com.example.viewmodel.AnimeVideoViewModel
import com.example.viewmodel.AppNavigationTab

class MainActivity : ComponentActivity() {

    private val viewModel: AnimeVideoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimeVideoAITheme {
                AnimeVideoApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AnimeVideoApp(viewModel: AnimeVideoViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val formState by viewModel.formState.collectAsState()
    val pipelineState by viewModel.pipelineState.collectAsState()
    val projects by viewModel.allProjects.collectAsState()
    val selectedScenes by viewModel.selectedProjectScenes.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val playheadSeconds by viewModel.currentPlayheadSeconds.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .statusBarsPadding(),
        containerColor = SleekBackground,
        bottomBar = {
            SleekBottomNavBar(
                currentTab = selectedTab,
                onSelectTab = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppNavigationTab.STUDIO -> StudioHomeScreen(
                    viewModel = viewModel,
                    formState = formState,
                    pipelineState = pipelineState,
                    isPlaying = isPlaying,
                    playheadSeconds = playheadSeconds,
                    onOpenApiSettings = { viewModel.selectTab(AppNavigationTab.API_BACKEND) }
                )
                AppNavigationTab.STORYBOARD -> StoryboardScreen(
                    scenes = selectedScenes,
                    storyPrompt = formState.storyPrompt,
                    characterConsistency = formState.characterConsistencyEnabled
                )
                AppNavigationTab.VOICE_AUDIO -> StudioHomeScreen(
                    viewModel = viewModel,
                    formState = formState,
                    pipelineState = pipelineState,
                    isPlaying = isPlaying,
                    playheadSeconds = playheadSeconds,
                    onOpenApiSettings = { viewModel.selectTab(AppNavigationTab.API_BACKEND) }
                )
                AppNavigationTab.PROJECTS -> GalleryScreen(
                    projects = projects,
                    onSelectProject = { viewModel.selectProject(it) },
                    onDeleteProject = { viewModel.deleteProject(it) }
                )
                AppNavigationTab.API_BACKEND -> BackendApiScreen(
                    formState = formState,
                    onUpdateReplicateTokenInput = { viewModel.updateCustomReplicateToken(it) },
                    onSaveReplicateToken = { viewModel.saveReplicateToken() },
                    onUpdateHindiVoiceKeyInput = { viewModel.updateCustomHindiVoiceKey(it) },
                    onSaveHindiVoiceKey = { viewModel.saveHindiVoiceKey() },
                    onClearKeys = { viewModel.clearStoredSecrets() }
                )
            }
        }
    }
}

@Composable
fun SleekBottomNavBar(
    currentTab: AppNavigationTab,
    onSelectTab: (AppNavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SleekSurface.copy(alpha = 0.95f))
            .border(
                width = 1.dp,
                color = SleekSlate800,
                shape = androidx.compose.ui.graphics.RectangleShape
            )
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .testTag("sleek_bottom_nav_bar"),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val navItems = listOf(
            Triple(AppNavigationTab.STUDIO, "Studio", Icons.Default.Home),
            Triple(AppNavigationTab.STORYBOARD, "Scenes", Icons.Default.Layers),
            Triple(AppNavigationTab.PROJECTS, "Library", Icons.Default.VideoLibrary),
            Triple(AppNavigationTab.API_BACKEND, "Settings", Icons.Default.Settings)
        )

        navItems.forEach { (tab, label, icon) ->
            val isSelected = currentTab == tab

            Column(
                modifier = Modifier
                    .clickable { onSelectTab(tab) }
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .testTag("nav_item_${tab.name.lowercase()}"),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) SleekPurpleLight else SleekSlate500,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    ),
                    color = if (isSelected) SleekPurpleLight else SleekSlate500
                )
            }
        }
    }
}
