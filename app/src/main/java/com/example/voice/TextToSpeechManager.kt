package com.example.voice

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import java.util.Locale

class TextToSpeechManager(
    private val context: Context,
    private val onSpeakingStateChanged: (Boolean) -> Unit
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var pendingTextToSpeak: String? = null

    private var currentRate: Float = 0.95f
    private var currentPitch: Float = 1.15f

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            setupVoiceAndLanguage()
            setupUtteranceListener()
            pendingTextToSpeak?.let {
                speak(it, currentRate, currentPitch)
                pendingTextToSpeak = null
            }
        } else {
            Log.e("TextToSpeechManager", "TTS initialization failed with code $status")
            onSpeakingStateChanged(false)
        }
    }

    private fun setupVoiceAndLanguage() {
        val tts = tts ?: return
        val hindiLocale = Locale.forLanguageTag("hi-IN")
        val result = tts.setLanguage(hindiLocale)

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            Log.w("TextToSpeechManager", "Hindi language not supported, using default locale")
            tts.language = Locale.getDefault()
        }

        // Try to pick a pleasant female voice if available
        try {
            val voices = tts.voices
            if (voices != null && voices.isNotEmpty()) {
                val femaleVoice = voices.firstOrNull { voice ->
                    val name = voice.name.lowercase(Locale.ROOT)
                    (name.contains("hi") || voice.locale.language == "hi") &&
                            (name.contains("female") || name.contains("zira") || name.contains("f0") || name.contains("woman"))
                } ?: voices.firstOrNull { voice ->
                    voice.locale.language == "hi"
                } ?: voices.firstOrNull { voice ->
                    val name = voice.name.lowercase(Locale.ROOT)
                    name.contains("female") || name.contains("zira")
                }

                femaleVoice?.let {
                    tts.voice = it
                }
            }
        } catch (e: Exception) {
            Log.w("TextToSpeechManager", "Could not set custom voice: ${e.message}")
        }

        tts.setPitch(currentPitch)
        tts.setSpeechRate(currentRate)
    }

    private fun setupUtteranceListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                onSpeakingStateChanged(true)
            }

            override fun onDone(utteranceId: String?) {
                onSpeakingStateChanged(false)
            }

            override fun onError(utteranceId: String?) {
                onSpeakingStateChanged(false)
            }
        })
    }

    fun speak(text: String, rate: Float = currentRate, pitch: Float = currentPitch) {
        currentRate = rate
        currentPitch = pitch

        if (!isInitialized) {
            pendingTextToSpeak = text
            return
        }

        val ttsInstance = tts ?: return
        ttsInstance.setSpeechRate(rate)
        ttsInstance.setPitch(pitch)

        // Strip markdown / emojis for cleaner pronunciation if needed, or speak full clean text
        val cleanText = text.replace(Regex("[\uD83C-\uDBFF\uDC00-\uDFFF]+"), "") // remove high emojis for TTS engine stability
            .trim()
        val textToSpeak = if (cleanText.isBlank()) text else cleanText

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MAYA_UTTERANCE_${System.currentTimeMillis()}")
        }

        ttsInstance.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params, "MAYA_UTTERANCE")
    }

    fun stop() {
        tts?.stop()
        onSpeakingStateChanged(false)
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("TextToSpeechManager", "Error shutting down TTS", e)
        }
    }
}
