package com.example.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

class SpeechRecognizerHelper(
    private val context: Context,
    private val onListeningStateChanged: (Boolean) -> Unit,
    private val onRmsChanged: (Float) -> Unit,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit
) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false

    init {
        initRecognizer()
    }

    private fun initRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        isListening = true
                        onListeningStateChanged(true)
                    }

                    override fun onBeginningOfSpeech() {
                        isListening = true
                        onListeningStateChanged(true)
                    }

                    override fun onRmsChanged(rmsdB: Float) {
                        // Normalize rmsdB (typically -2 to 10 dB) to 0f..1f range for animation
                        val normalized = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
                        onRmsChanged(normalized)
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        isListening = false
                        onListeningStateChanged(false)
                        onRmsChanged(0f)
                    }

                    override fun onError(error: Int) {
                        isListening = false
                        onListeningStateChanged(false)
                        onRmsChanged(0f)

                        val errorMessage = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "ऑडियो रिकॉर्डिंग में समस्या हुई"
                            SpeechRecognizer.ERROR_CLIENT -> "क्लाइंट एरर"
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "माइक्रोफ़ोन अनुमति आवश्यक है"
                            SpeechRecognizer.ERROR_NETWORK -> "इंटरनेट कनेक्शन की जाँच करें"
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "नेटवर्क टाइमआउट हो गया"
                            SpeechRecognizer.ERROR_NO_MATCH -> "कुछ सुनाई नहीं दिया, कृपया दोबारा बोलें"
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "स्पीच इंजन व्यस्त है"
                            SpeechRecognizer.ERROR_SERVER -> "सर्वर से उत्तर नहीं मिला"
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "आवाज़ नहीं आई, माइक बटन दबाकर बोलें"
                            else -> "त्रुटि कोड: $error"
                        }
                        Log.w("SpeechRecognizerHelper", "Recognition error: $errorMessage ($error)")
                        if (error != SpeechRecognizer.ERROR_NO_MATCH && error != SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
                            onError(errorMessage)
                        }
                    }

                    override fun onResults(results: Bundle?) {
                        isListening = false
                        onListeningStateChanged(false)
                        onRmsChanged(0f)

                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            val spokenText = matches[0]
                            onResult(spokenText)
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            // Can show live preview if desired
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
        } else {
            Log.w("SpeechRecognizerHelper", "SpeechRecognizer is not available on this device")
        }
    }

    fun startListening() {
        if (speechRecognizer == null) {
            initRecognizer()
        }

        val recognizer = speechRecognizer ?: run {
            onError("इस डिवाइस पर स्पीच रिकॉग्निशन उपलब्ध नहीं है। आप टाइप करके बात कर सकते हैं!")
            return
        }

        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "माया से बोलिए... (हिंदी)")
            }
            recognizer.startListening(intent)
            isListening = true
            onListeningStateChanged(true)
        } catch (e: Exception) {
            Log.e("SpeechRecognizerHelper", "Failed to start listening", e)
            isListening = false
            onListeningStateChanged(false)
            onError("माइक शुरू करने में समस्या हुई: ${e.localizedMessage}")
        }
    }

    fun stopListening() {
        if (isListening) {
            try {
                speechRecognizer?.stopListening()
            } catch (e: Exception) {
                Log.e("SpeechRecognizerHelper", "Failed to stop listening", e)
            }
            isListening = false
            onListeningStateChanged(false)
            onRmsChanged(0f)
        }
    }

    fun destroy() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (e: Exception) {
            Log.e("SpeechRecognizerHelper", "Failed to destroy speech recognizer", e)
        }
    }
}
