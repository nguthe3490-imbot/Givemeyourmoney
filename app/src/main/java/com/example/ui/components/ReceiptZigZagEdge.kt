package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Renders a jagged zig-zag cut edge for thermal receipt paper.
 */
@Composable
fun ReceiptZigZagEdge(
    modifier: Modifier = Modifier,
    isTop: Boolean = true,
    paperColor: Color = Color(0xFFFFFDF5),
    backgroundColor: Color = Color(0xFF130D1E),
    toothWidth: Dp = 10.dp,
    height: Dp = 8.dp
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val toothWidthPx = toothWidth.toPx()
        val h = size.height
        val w = size.width
        val count = (w / toothWidthPx).toInt() + 2

        val path = Path()

        if (isTop) {
            // Background fills top, teeth cut down into paper
            path.moveTo(0f, 0f)
            for (i in 0 until count) {
                val startX = i * toothWidthPx
                val midX = startX + toothWidthPx / 2
                val endX = startX + toothWidthPx
                path.lineTo(midX, h)
                path.lineTo(endX, 0f)
            }
            path.lineTo(w, h)
            path.lineTo(0f, h)
            path.close()
        } else {
            // Teeth cut down at bottom
            path.moveTo(0f, 0f)
            for (i in 0 until count) {
                val startX = i * toothWidthPx
                val midX = startX + toothWidthPx / 2
                val endX = startX + toothWidthPx
                path.lineTo(midX, h)
                path.lineTo(endX, 0f)
            }
            path.lineTo(w, 0f)
            path.close()
        }

        drawPath(
            path = path,
            color = if (isTop) paperColor else paperColor
        )
    }
}
