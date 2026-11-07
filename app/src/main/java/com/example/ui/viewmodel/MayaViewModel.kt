package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.engine.MayaAiEngine
import com.example.data.model.AssistantEmotion
import com.example.data.model.AssistantSettings
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.voice.SpeechRecognizerHelper
import com.example.voice.TextToSpeechManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MayaUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isListening: Boolean = false,
    val isSpeaking: Boolean = false,
    val audioRmsLevel: Float = 0f,
    val statusText: String = "आपकी सेवा में हमेशा हाज़िर 💖",
    val currentInput: String = "",
    val settings: AssistantSettings = AssistantSettings(),
    val selectedTab: Int = 0,
    val snackbarMessage: String? = null
)

class MayaViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MayaUiState())
    val uiState: StateFlow<MayaUiState> = _uiState.asStateFlow()

    private var ttsManager: TextToSpeechManager? = null
    private var speechRecognizerHelper: SpeechRecognizerHelper? = null

    init {
        initVoiceServices(application)
        initWelcomeMessage()
    }

    private fun initVoiceServices(context: Application) {
        ttsManager = TextToSpeechManager(context) { speaking ->
            _uiState.update { current ->
                current.copy(
                    isSpeaking = speaking,
                    statusText = if (speaking) {
                        "${current.settings.assistantName} बोल रही है... 💖"
                    } else if (current.isListening) {
                        "सुन रही हूँ... बोलिए 🎙️"
                    } else {
                        "आपकी सेवा में हमेशा हाज़िर 💖"
                    }
                )
            }
        }

        speechRecognizerHelper = SpeechRecognizerHelper(
            context = context,
            onListeningStateChanged = { listening ->
                _uiState.update { current ->
                    current.copy(
                        isListening = listening,
                        statusText = if (listening) {
                            "सुन रही हूँ... बोलिए 🎙️"
                        } else if (current.isSpeaking) {
                            "${current.settings.assistantName} बोल रही है... 💖"
                        } else {
                            "आपकी सेवा में हमेशा हाज़िर 💖"
                        }
                    )
                }
            },
            onRmsChanged = { rms ->
                _uiState.update { it.copy(audioRmsLevel = rms) }
            },
            onResult = { recognizedText ->
                if (recognizedText.isNotBlank()) {
                    sendMessage(recognizedText)
                }
            },
            onError = { errorMsg ->
                _uiState.update {
                    it.copy(
                        snackbarMessage = errorMsg,
                        statusText = "माइक में आवाज़ नहीं आई, पुनः प्रयास करें"
                    )
                }
            }
        )
    }

    private fun initWelcomeMessage() {
        val welcomeText = "नमस्ते! मैं माया हूँ, आपकी अपनी पर्सनल एआई। बताइए आज आपके लिए क्या करूँ? 💖"
        val initialMessage = ChatMessage(
            sender = MessageSender.ASSISTANT,
            text = welcomeText,
            emotion = AssistantEmotion.GREETING,
            isSpoken = false
        )
        _uiState.update { it.copy(messages = listOf(initialMessage)) }

        // Give a tiny moment for TTS to be ready, then greet
        viewModelScope.launch {
            delay(1000)
            if (_uiState.value.settings.autoSpeak) {
                ttsManager?.speak(
                    text = welcomeText,
                    rate = _uiState.value.settings.speechRate,
                    pitch = _uiState.value.settings.speechPitch
                )
            }
        }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(currentInput = text) }
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun sendMessage(text: String = _uiState.value.currentInput) {
        val trimmed = text.trim()
        if (trimmed.isBlank()) return

        // Stop current speech before new interaction
        ttsManager?.stop()

        val userMessage = ChatMessage(
            sender = MessageSender.USER,
            text = trimmed
        )

        _uiState.update { current ->
            current.copy(
                messages = current.messages + userMessage,
                currentInput = "",
                statusText = "${current.settings.assistantName} सोच रही है... ✨"
            )
        }

        viewModelScope.launch {
            delay(300) // slight natural pause
            val response = MayaAiEngine.generateResponse(trimmed, _uiState.value.settings)
            val assistantMessage = ChatMessage(
                sender = MessageSender.ASSISTANT,
                text = response.replyText,
                emotion = response.emotion,
                isSpoken = _uiState.value.settings.autoSpeak
            )

            _uiState.update { current ->
                current.copy(
                    messages = current.messages + assistantMessage,
                    statusText = if (current.settings.autoSpeak) {
                        "${current.settings.assistantName} बोल रही है... 💖"
                    } else {
                        "आपकी सेवा में हमेशा हाज़िर 💖"
                    }
                )
            }

            if (_uiState.value.settings.autoSpeak) {
                ttsManager?.speak(
                    text = response.replyText,
                    rate = _uiState.value.settings.speechRate,
                    pitch = _uiState.value.settings.speechPitch
                )
            }
        }
    }

    fun toggleVoiceListening() {
        if (_uiState.value.isListening) {
            speechRecognizerHelper?.stopListening()
        } else {
            ttsManager?.stop()
            speechRecognizerHelper?.startListening()
        }
    }

    fun speakMessage(message: ChatMessage) {
        ttsManager?.speak(
            text = message.text,
            rate = _uiState.value.settings.speechRate,
            pitch = _uiState.value.settings.speechPitch
        )
    }

    fun stopSpeaking() {
        ttsManager?.stop()
    }

    fun updateSettings(newSettings: AssistantSettings) {
        val oldName = _uiState.value.settings.assistantName
        _uiState.update { it.copy(settings = newSettings) }

        if (oldName != newSettings.assistantName) {
            val announcement = "जी $newSettings.userCallName, अब से मुझे ${newSettings.assistantName} कहकर पुकारिएगा! 💖"
            val notice = ChatMessage(
                sender = MessageSender.ASSISTANT,
                text = announcement,
                emotion = AssistantEmotion.LOVE
            )
            _uiState.update { it.copy(messages = it.messages + notice) }
            if (newSettings.autoSpeak) {
                ttsManager?.speak(announcement, newSettings.speechRate, newSettings.speechPitch)
            }
        }
    }

    fun testVoice(text: String = "नमस्ते जान! मेरी आवाज़ आपको कैसी लग रही है?") {
        ttsManager?.speak(
            text = text,
            rate = _uiState.value.settings.speechRate,
            pitch = _uiState.value.settings.speechPitch
        )
    }

    fun clearChat() {
        val resetGreeting = "चैट साफ कर दी गई है ${_uiState.value.settings.userCallName}! अब क्या बात करना चाहेंगे? 🌸"
        val message = ChatMessage(
            sender = MessageSender.ASSISTANT,
            text = resetGreeting,
            emotion = AssistantEmotion.HAPPY
        )
        _uiState.update { it.copy(messages = listOf(message)) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager?.shutdown()
        speechRecognizerHelper?.destroy()
    }
}
