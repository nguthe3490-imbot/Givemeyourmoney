package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent

@Composable
fun OxyBar(
    oxyLevel: Int, // 0 to 100
    canBreatheForever: Boolean,
    onRefillOxyClick: () -> Unit,
    onPayToBreatheClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val barColor by animateColorAsState(
        targetValue = when {
            canBreatheForever -> Color(0xFF00E5FF)
            oxyLevel > 50 -> Color(0xFF00C853)
            oxyLevel > 20 -> Color(0xFFFFAB00)
            else -> NeonRedAccent
        },
        label = "oxy_color"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF1E132B), RoundedCornerShape(12.dp))
            .border(1.dp, barColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (canBreatheForever) Icons.Default.Shield else Icons.Default.Air,
                        contentDescription = "Oxy",
                        tint = barColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (canBreatheForever) "Bảo Hiểm Oxy Vĩnh Cửu (VIP)" else "Thuế Hít Thở Trong Game",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (canBreatheForever) "100%" else "$oxyLevel%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = barColor
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { if (canBreatheForever) 1f else (oxyLevel / 100f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = barColor,
                            trackColor = Color.DarkGray
                        )
                    }
                }

                if (!canBreatheForever) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onPayToBreatheClick?.invoke() ?: onRefillOxyClick() },
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("refill_oxy_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonRedAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Nạp tiền để tiếp tục thở",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
