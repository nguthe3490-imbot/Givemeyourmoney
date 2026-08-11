package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class SatiricalTtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("vi", "VN"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to default locale if Vietnamese TTS data is missing
                tts?.language = Locale.getDefault()
            }
            tts?.setSpeechRate(0.95f) // Slightly faster, sarcastic tone
            tts?.setPitch(1.1f)      // High cheeky pitch
            isReady = true
        } else {
            Log.e("SatiricalTtsManager", "TTS Initialization failed")
        }
    }

    fun speak(text: String) {
        if (isReady && tts != null && text.isNotBlank()) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "PVP_SATIRE_TTS")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
