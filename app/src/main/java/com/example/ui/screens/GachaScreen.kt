package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.ui.theme.DarkGold
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent
import com.example.ui.theme.VipPurple
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GachaScreen(
    user: UserEntity?,
    systemNotice: String,
    onSpinClick: () -> Unit,
    onSpinWithGoldClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    val rotationAnim = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val wheelItems = listOf(
        "Mảnh Đao Gỗ",
        "Lời Cảm Ơn NPH",
        "0.0001 Kính Slime",
        "Vé Nạp Tiếp",
        "Giảm Giá 0%",
        "Trúng 0.1 Vàng"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF140D1E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Casino,
                contentDescription = "Gacha",
                tint = GoldPrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "VÒNG QUAY 'HÚT MÁU' NPH",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = GoldPrimary
                )
                Text(
                    text = "Tỷ lệ trúng đồ xịn: 0.00000001% (Tùy số tiền nạp)",
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
            }
        }

        // Gems Counter Badge
        Box(
            modifier = Modifier
                .background(VipPurple, RoundedCornerShape(20.dp))
                .border(1.dp, GoldPrimary, RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Gems",
                    tint = Color(0xFF00E5FF),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Ngọc Nạp Hiện Có: ${currencyFormat.format(user?.gems ?: 0)}",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }

        // Animated Wheel Canvas
        Box(
            modifier = Modifier
                .size(260.dp)
                .testTag("gacha_wheel_box"),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(rotationAnim.value)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2
                val sweepAngle = 360f / wheelItems.size

                val colors = listOf(
                    Color(0xFF3B1E54), Color(0xFF8B0000), Color(0xFF2E1B3A),
                    Color(0xFFB8860B), Color(0xFF4A148C), Color(0xFF800000)
                )

                wheelItems.forEachIndexed { index, _ ->
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = index * sweepAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        size = Size(radius * 2, radius * 2),
                        topLeft = Offset(center.x - radius, center.y - radius)
                    )
                }
            }

            // Outer Golden Frame
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(6.dp, Brush.sweepGradient(listOf(GoldPrimary, DarkGold, GoldPrimary)), CircleShape)
            )

            // Center Spin Needle / Badge
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(GoldPrimary, CircleShape)
                    .border(3.dp, NeonRedAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "P2W",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }

        // Result / System Message
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF221633)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = systemNotice,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )
        }

        // Spin Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Gold Spin
            Button(
                onClick = {
                    scope.launch {
                        rotationAnim.animateTo(
                            targetValue = rotationAnim.value + 1440f + (0..360).random(),
                            animationSpec = tween(durationMillis = 2000)
                        )
                        onSpinWithGoldClick()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("spin_gacha_gold_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD4AF37),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "QUAY VÀNG (1,000 Vàng)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            // Gems Spin
            Button(
                onClick = {
                    scope.launch {
                        rotationAnim.animateTo(
                            targetValue = rotationAnim.value + 1440f + (0..360).random(),
                            animationSpec = tween(durationMillis = 2000)
                        )
                        onSpinClick()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("spin_gacha_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "QUAY NGỌC (100 Ngọc)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}
