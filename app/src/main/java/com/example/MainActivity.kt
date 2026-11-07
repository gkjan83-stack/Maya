package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.ChatBubbleItem
import com.example.ui.components.GlowingOrbVisualizer
import com.example.ui.components.QuickPromptsRow
import com.example.ui.screens.FunCenterScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.DarkBgPrimary
import com.example.ui.theme.DarkBgSecondary
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.MayaCyanAccent
import com.example.ui.theme.MayaPinkPrimary
import com.example.ui.theme.MayaPinkSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextLightPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.MayaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MayaApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MayaApp(viewModel: MayaViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Audio record permission launcher
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggleVoiceListening()
        } else {
            Toast.makeText(
                context,
                "आवाज़ पहचानने के लिए माइक्रोफ़ोन की अनुमति चाहिए। आप लिख कर भी बात कर सकते हैं!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val onMicAction = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            viewModel.toggleVoiceListening()
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBgPrimary,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(listOf(MayaPinkPrimary, MayaCyanAccent))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.settings.assistantName.take(1),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = uiState.settings.assistantName,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "💖", fontSize = 14.sp)
                            }
                            Text(
                                text = "हिंदी AI वॉइस असिस्टेंट",
                                color = MayaPinkSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                actions = {
                    // Voice stop action if speaking
                    if (uiState.isSpeaking) {
                        IconButton(
                            onClick = { viewModel.stopSpeaking() },
                            modifier = Modifier.testTag("stop_voice_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "आवाज़ रोकें",
                                tint = MayaPinkPrimary
                            )
                        }
                    }

                    // Clear Chat icon
                    if (uiState.selectedTab == 0 && uiState.messages.size > 1) {
                        IconButton(
                            onClick = { viewModel.clearChat() },
                            modifier = Modifier.testTag("clear_chat_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "चैट साफ करें",
                                tint = TextMuted
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBgSecondary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(DarkBgSecondary)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                // If on Chat screen, show the input bar above navigation
                if (uiState.selectedTab == 0) {
                    ChatInputBar(
                        input = uiState.currentInput,
                        isListening = uiState.isListening,
                        assistantName = uiState.settings.assistantName,
                        onInputChange = { viewModel.onInputChange(it) },
                        onSendMessage = { viewModel.sendMessage() },
                        onMicClick = onMicAction
                    )
                }

                // Bottom Navigation Tabs
                NavigationBar(
                    containerColor = DarkBgSecondary,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets(0, 0, 0, 0)
                ) {
                    NavigationBarItem(
                        selected = uiState.selectedTab == 0,
                        onClick = { viewModel.onTabSelected(0) },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = "बातचीत"
                            )
                        },
                        label = { Text("बातचीत") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MayaPinkSecondary,
                            indicatorColor = MayaPinkPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        )
                    )

                    NavigationBarItem(
                        selected = uiState.selectedTab == 1,
                        onClick = { viewModel.onTabSelected(1) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "शायरी & मस्ती"
                            )
                        },
                        label = { Text("शायरी & मस्ती") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MayaPinkSecondary,
                            indicatorColor = MayaPinkPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        )
                    )

                    NavigationBarItem(
                        selected = uiState.selectedTab == 2,
                        onClick = { viewModel.onTabSelected(2) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "सेटिंग्स"
                            )
                        },
                        label = { Text("सेटिंग्स") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MayaPinkSecondary,
                            indicatorColor = MayaPinkPrimary,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            DarkBgSecondary,
                            DarkBgPrimary,
                            Color(0xFF130822)
                        )
                    )
                )
        ) {
            when (uiState.selectedTab) {
                0 -> ChatAndVoiceScreen(
                    uiState = uiState,
                    onOrbClick = onMicAction,
                    onPromptClick = { viewModel.sendMessage(it) },
                    onSpeakMessage = { viewModel.speakMessage(it) }
                )
                1 -> FunCenterScreen(
                    assistantName = uiState.settings.assistantName,
                    onTopicSelected = {
                        viewModel.onTabSelected(0)
                        viewModel.sendMessage(it)
                    }
                )
                2 -> SettingsScreen(
                    settings = uiState.settings,
                    onSettingsChanged = { viewModel.updateSettings(it) },
                    onTestVoice = { viewModel.testVoice() },
                    onClearChat = { viewModel.clearChat() }
                )
            }
        }
    }
}

@Composable
fun ChatAndVoiceScreen(
    uiState: com.example.ui.viewmodel.MayaUiState,
    onOrbClick: () -> Unit,
    onPromptClick: (String) -> Unit,
    onSpeakMessage: (com.example.data.model.ChatMessage) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Upper Visualizer Area
        GlowingOrbVisualizer(
            isListening = uiState.isListening,
            isSpeaking = uiState.isSpeaking,
            audioRmsLevel = uiState.audioRmsLevel,
            statusText = uiState.statusText,
            assistantName = uiState.settings.assistantName,
            onOrbClick = onOrbClick,
            modifier = Modifier.fillMaxWidth()
        )

        // Quick Suggestions Row
        QuickPromptsRow(
            assistantName = uiState.settings.assistantName,
            onPromptClick = onPromptClick
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Chat Message History List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = uiState.messages,
                key = { it.id }
            ) { message ->
                ChatBubbleItem(
                    message = message,
                    assistantName = uiState.settings.assistantName,
                    onSpeakClick = { onSpeakMessage(message) }
                )
            }
        }
    }
}

@Composable
fun ChatInputBar(
    input: String,
    isListening: Boolean,
    assistantName: String,
    onInputChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onMicClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .imePadding(),
        color = DarkCardElevated,
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated Voice Mic Button
            FloatingActionButton(
                onClick = onMicClick,
                modifier = Modifier
                    .size(46.dp)
                    .testTag("voice_mic_button"),
                shape = CircleShape,
                containerColor = if (isListening) MayaCyanAccent else MayaPinkPrimary,
                contentColor = if (isListening) Color.Black else Color.White
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                    contentDescription = if (isListening) "माइक बंद करें" else "बोलकर कहें",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Text Input Field
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                placeholder = {
                    Text(
                        text = "$assistantName से कुछ कहें... (हिंदी)",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MayaPinkPrimary,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkCardBg,
                    unfocusedContainerColor = DarkCardBg
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Send Button
            IconButton(
                onClick = onSendMessage,
                enabled = input.isNotBlank(),
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (input.isNotBlank()) MayaPinkPrimary else Color.Transparent
                    )
                    .testTag("send_message_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "भेजें",
                    tint = if (input.isNotBlank()) Color.White else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
