package com.example.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.flow.SharedFlow
import kotlin.math.sin
import kotlin.random.Random

data class MoneyParticle(
    val id: Long,
    var xRatio: Float,       // 0f..1f relative to screen width
    var yRatio: Float,       // -0.3f..1.2f relative to screen height
    val speedY: Float,       // vertical velocity
    val speedX: Float,       // horizontal drift velocity
    val sizeSp: Float,       // size in sp
    val symbol: String,      // "🪙", "💵", "💰", "💲", "💸", "🤑"
    var rotation: Float,     // degrees
    val rotationSpeed: Float,// rotation delta
    val swayAmp: Float,      // horizontal sway amplitude
    val swayFreq: Float,     // sway frequency
    var alpha: Float = 1f    // opacity
)

@Composable
fun MoneyParticleOverlay(
    moneyRainEvent: SharedFlow<Unit>,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    val particles = remember { mutableStateListOf<MoneyParticle>() }

    // Listen for trigger events to spawn bursts of falling coins/money
    LaunchedEffect(moneyRainEvent) {
        moneyRainEvent.collect {
            val count = Random.nextInt(45, 75)
            val now = System.currentTimeMillis()
            val symbols = listOf("🪙", "💵", "💰", "💲", "💸", "🤑", "💵", "🪙", "💲")

            val newBurst = List(count) { index ->
                MoneyParticle(
                    id = now + index + Random.nextLong(100000),
                    xRatio = Random.nextFloat() * 0.96f + 0.02f,
                    yRatio = -(Random.nextFloat() * 0.4f + 0.05f),
                    speedY = Random.nextFloat() * 0.012f + 0.008f,
                    speedX = (Random.nextFloat() - 0.5f) * 0.002f,
                    sizeSp = Random.nextFloat() * 20f + 22f,
                    symbol = symbols.random(),
                    rotation = Random.nextFloat() * 360f,
                    rotationSpeed = (Random.nextFloat() - 0.5f) * 8f,
                    swayAmp = Random.nextFloat() * 0.03f + 0.01f,
                    swayFreq = Random.nextFloat() * 8f + 4f,
                    alpha = 1f
                )
            }
            particles.addAll(newBurst)
        }
    }

    // Animation frame loop
    LaunchedEffect(particles) {
        while (true) {
            withFrameNanos { _ ->
                if (particles.isNotEmpty()) {
                    val iterator = particles.iterator()
                    while (iterator.hasNext()) {
                        val p = iterator.next()
                        p.yRatio += p.speedY
                        p.xRatio += p.speedX + sin(p.yRatio * p.swayFreq) * (p.swayAmp * 0.1f)
                        p.rotation += p.rotationSpeed

                        if (p.yRatio > 0.9f) {
                            p.alpha -= 0.04f
                        }

                        if (p.yRatio > 1.15f || p.alpha <= 0f) {
                            iterator.remove()
                        }
                    }
                }
            }
        }
    }

    if (particles.isEmpty()) return

    val paint = remember {
        Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val widthPx = size.width
        val heightPx = size.height

        drawIntoCanvas { canvas ->
            val nativeCanvas = canvas.nativeCanvas
            particles.toList().forEach { p ->
                val xPx = p.xRatio * widthPx
                val yPx = p.yRatio * heightPx
                val textSizePx = p.sizeSp * density

                paint.textSize = textSizePx
                paint.alpha = (p.alpha * 255).toInt().coerceIn(0, 255)

                nativeCanvas.save()
                nativeCanvas.rotate(p.rotation, xPx, yPx)
                nativeCanvas.drawText(p.symbol, xPx, yPx, paint)
                nativeCanvas.restore()
            }
        }
    }
}
