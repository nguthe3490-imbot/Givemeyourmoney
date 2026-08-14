package com.example.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

enum class MoneySoundType {
    TING_TING,        // Tiếng leng keng đồng tiền nạp VIP
    MONEY_COUNTER,    // Tiếng máy đếm tiền xoẹt xoẹt rào rạt
    CASH_REGISTER,    // Tiếng két sắt Ka-Ching mở tiền
    JACKPOT_CELEBRATE // Tiếng đại gia vung tiền
}

/**
 * PaySoundEffects generates high-fidelity procedural audio for P2W money sounds:
 * - "Ting Ting" coin clinks & cash chimes
 * - "Tiếng máy đếm tiền" (rapid bill counting flutters + chime)
 * - "Ka-ching" cash register bell
 *
 * Runs asynchronously via AudioTrack without requiring any external mp3/wav files.
 */
object PaySoundEffects {

    private const val SAMPLE_RATE = 44100
    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Plays the ting-ting or money counter sound effect asynchronously.
     */
    fun play(type: MoneySoundType = MoneySoundType.TING_TING) {
        scope.launch {
            try {
                when (type) {
                    MoneySoundType.TING_TING -> playTingTingInternal()
                    MoneySoundType.MONEY_COUNTER -> playMoneyCounterInternal()
                    MoneySoundType.CASH_REGISTER -> playCashRegisterInternal()
                    MoneySoundType.JACKPOT_CELEBRATE -> playJackpotInternal()
                }
            } catch (e: Exception) {
                Log.e("PaySoundEffects", "Failed to play money sound", e)
            }
        }
    }

    /**
     * Synthesizes a crisp, bright "Ting... Ting!" double coin clink chime.
     */
    private fun playTingTingInternal() {
        val durationSec = 0.55f
        val numSamples = (SAMPLE_RATE * durationSec).toInt()
        val buffer = ShortArray(numSamples)

        // Strike 1 starts at 0ms
        val strike1Start = 0
        // Strike 2 starts at ~85ms
        val strike2Start = (SAMPLE_RATE * 0.085f).toInt()

        for (i in 0 until numSamples) {
            var sample = 0f

            // First "Ting" (Higher frequency bright chime)
            if (i >= strike1Start) {
                val t1 = (i - strike1Start).toFloat() / SAMPLE_RATE
                val env1 = exp(-t1 * 22f) // rapid decay
                if (env1 > 0.001f) {
                    val tone1 = sin(2.0 * PI * 2093.0 * t1).toFloat() * 0.6f + // C7
                            sin(2.0 * PI * 3136.0 * t1).toFloat() * 0.35f + // G7
                            sin(2.0 * PI * 4186.0 * t1).toFloat() * 0.25f   // C8
                    sample += tone1 * env1 * 0.7f
                }
            }

            // Second "Ting!" (Resonant coin bell ring)
            if (i >= strike2Start) {
                val t2 = (i - strike2Start).toFloat() / SAMPLE_RATE
                val env2 = exp(-t2 * 9f) // longer resonant sustain
                if (env2 > 0.001f) {
                    val tone2 = sin(2.0 * PI * 2793.8 * t2).toFloat() * 0.65f + // F7
                            sin(2.0 * PI * 3520.0 * t2).toFloat() * 0.45f + // A7
                            sin(2.0 * PI * 5587.6 * t2).toFloat() * 0.3f    // F8
                    sample += tone2 * env2 * 0.85f
                }
            }

            // Hard clamp to prevent clipping
            val clamped = sample.coerceIn(-1.0f, 1.0f)
            buffer[i] = (clamped * Short.MAX_VALUE * 0.85f).toInt().toShort()
        }

        playPcmBuffer(buffer)
    }

    /**
     * Synthesizes "Tiếng máy đếm tiền" (rapid mechanical paper bill counting flutters + confirmation chime).
     */
    private fun playMoneyCounterInternal() {
        val durationSec = 0.85f
        val numSamples = (SAMPLE_RATE * durationSec).toInt()
        val buffer = ShortArray(numSamples)

        // Generate 12 rapid bill-counting "whir/flaps"
        val numBills = 14
        val billInterval = (SAMPLE_RATE * 0.038f).toInt() // ~38ms per bill counted

        for (i in 0 until numSamples) {
            var sample = 0f

            // Bill counting flutter clicks
            val billIndex = i / billInterval
            if (billIndex < numBills) {
                val billOffset = i % billInterval
                val tBill = billOffset.toFloat() / SAMPLE_RATE
                val clickEnv = exp(-tBill * 140f) // very short sharp click
                if (clickEnv > 0.001f) {
                    // Friction paper noise + mechanical pitch
                    val noise = (Random.nextFloat() * 2f - 1f) * 0.45f
                    val clickFreq = 1600.0 + (billIndex * 60.0) // pitch slightly climbs as counter speeds up
                    val clickTone = sin(2.0 * PI * clickFreq * tBill).toFloat() * 0.55f
                    sample += (noise + clickTone) * clickEnv * 0.75f
                }
            }

            // Final completion beep / chime at the end of the stack
            val chimeStart = (numBills * billInterval) - (SAMPLE_RATE * 0.04f).toInt()
            if (i >= chimeStart) {
                val tChime = (i - chimeStart).toFloat() / SAMPLE_RATE
                val chimeEnv = exp(-tChime * 10f)
                if (chimeEnv > 0.001f) {
                    val chimeTone = sin(2.0 * PI * 2637.0 * tChime).toFloat() * 0.6f + // E7
                            sin(2.0 * PI * 3951.0 * tChime).toFloat() * 0.4f + // B7
                            sin(2.0 * PI * 5274.0 * tChime).toFloat() * 0.25f  // E8
                    sample += chimeTone * chimeEnv * 0.9f
                }
            }

            val clamped = sample.coerceIn(-1.0f, 1.0f)
            buffer[i] = (clamped * Short.MAX_VALUE * 0.85f).toInt().toShort()
        }

        playPcmBuffer(buffer)
    }

    /**
     * Synthesizes the classic Cash Register "Ka-Ching!" drawer sound.
     */
    private fun playCashRegisterInternal() {
        val durationSec = 0.7f
        val numSamples = (SAMPLE_RATE * durationSec).toInt()
        val buffer = ShortArray(numSamples)

        val bellStart = (SAMPLE_RATE * 0.06f).toInt()

        for (i in 0 until numSamples) {
            var sample = 0f

            // Mechanical latch / drawer opening ("Ka-")
            if (i < bellStart + (SAMPLE_RATE * 0.04f)) {
                val tLatch = i.toFloat() / SAMPLE_RATE
                val latchEnv = exp(-tLatch * 80f)
                val noise = (Random.nextFloat() * 2f - 1f) * 0.5f
                val thud = sin(2.0 * PI * 320.0 * tLatch).toFloat() * 0.5f
                sample += (noise + thud) * latchEnv * 0.6f
            }

            // High metallic cash register bell ring ("-Ching!")
            if (i >= bellStart) {
                val tBell = (i - bellStart).toFloat() / SAMPLE_RATE
                val bellEnv = exp(-tBell * 7.5f)
                if (bellEnv > 0.001f) {
                    val tone = sin(2.0 * PI * 2349.3 * tBell).toFloat() * 0.55f + // D7
                            sin(2.0 * PI * 3520.0 * tBell).toFloat() * 0.45f + // A7
                            sin(2.0 * PI * 4698.6 * tBell).toFloat() * 0.3f +  // D8
                            sin(2.0 * PI * 7040.0 * tBell).toFloat() * 0.15f   // A8
                    sample += tone * bellEnv * 0.85f
                }
            }

            val clamped = sample.coerceIn(-1.0f, 1.0f)
            buffer[i] = (clamped * Short.MAX_VALUE * 0.85f).toInt().toShort()
        }

        playPcmBuffer(buffer)
    }

    /**
     * Synthesizes a celebratory VIP Jackpot fanfare sequence.
     */
    private fun playJackpotInternal() {
        val notes = listOf(1046.5, 1318.5, 1567.98, 2093.0, 2637.0, 3136.0) // C6, E6, G6, C7, E7, G7
        val noteDur = 0.07f
        val totalDur = (notes.size * noteDur) + 0.4f
        val numSamples = (SAMPLE_RATE * totalDur).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            var sample = 0f

            notes.forEachIndexed { idx, freq ->
                val start = (idx * noteDur * SAMPLE_RATE).toInt()
                if (i >= start) {
                    val t = (i - start).toFloat() / SAMPLE_RATE
                    val decayRate = if (idx == notes.lastIndex) 7f else 18f
                    val env = exp(-t * decayRate)
                    if (env > 0.001f) {
                        val tone = sin(2.0 * PI * freq * t).toFloat() * 0.6f +
                                sin(2.0 * PI * (freq * 2.0) * t).toFloat() * 0.3f
                        sample += tone * env * 0.65f
                    }
                }
            }

            val clamped = sample.coerceIn(-1.0f, 1.0f)
            buffer[i] = (clamped * Short.MAX_VALUE * 0.8f).toInt().toShort()
        }

        playPcmBuffer(buffer)
    }

    /**
     * Plays generated 16-bit PCM ShortArray using AudioTrack.
     */
    private fun playPcmBuffer(buffer: ShortArray) {
        var track: AudioTrack? = null
        try {
            val bufferSize = buffer.size * 2 // 2 bytes per short
            track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()

            // Wait until audio finished playing
            val playDurationMs = ((buffer.size.toFloat() / SAMPLE_RATE) * 1000).toLong() + 50
            Thread.sleep(playDurationMs)
        } catch (e: Exception) {
            Log.e("PaySoundEffects", "AudioTrack playback error", e)
        } finally {
            try {
                track?.stop()
                track?.release()
            } catch (e: Exception) {
                // Ignore cleanup errors
            }
        }
    }
}
