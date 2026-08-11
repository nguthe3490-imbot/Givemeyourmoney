package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.ui.theme.DarkGold
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent
import com.example.ui.theme.VipPurple
import java.text.NumberFormat
import java.util.Locale

@Composable
fun VipBanner(
    user: UserEntity?,
    onRechargeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2E1B3A),
                            Color(0xFF1B0F24)
                        )
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Top Row: User Name + VIP Level Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(listOf(GoldPrimary, DarkGold)),
                                CircleShape
                            )
                            .padding(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "VIP Icon",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = user?.username ?: "Nô Lệ Pay2Win",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "VIP ${user?.vipLevel ?: 0}",
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldPrimary,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = getVipTitle(user?.vipLevel ?: 0),
                                color = Color.LightGray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Recharge Fast Button
                Box(
                    modifier = Modifier
                        .scale(scalePulse)
                        .background(
                            Brush.horizontalGradient(listOf(NeonRedAccent, GoldPrimary)),
                            RoundedCornerShape(20.dp)
                        )
                        .border(1.5.dp, GoldPrimary, RoundedCornerShape(20.dp))
                        .clickable { onRechargeClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("recharge_fast_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Recharge",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "NẠP NGAY",
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Second Row: Currencies (Vàng Ảo, Ngọc Nạp, Tổng Cống Nộp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gold
                CurrencyItem(
                    icon = Icons.Default.MonetizationOn,
                    iconTint = GoldPrimary,
                    label = "Vàng Ảo",
                    value = currencyFormat.format(user?.gold ?: 0)
                )

                // Gems
                CurrencyItem(
                    icon = Icons.Default.Star,
                    iconTint = Color(0xFF00E5FF),
                    label = "Ngọc Nạp",
                    value = currencyFormat.format(user?.gems ?: 0)
                )

                // Total Spent
                Box(
                    modifier = Modifier
                        .background(VipPurple.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Tổng Cống Nộp",
                            fontSize = 9.sp,
                            color = Color.LightGray
                        )
                        Text(
                            text = "${currencyFormat.format(user?.totalSpentVnd ?: 0)}đ",
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // VIP EXP Progress Bar
            val exp = user?.vipExp ?: 0
            val maxExp = when {
                exp < 50 -> 50
                exp < 200 -> 200
                exp < 500 -> 500
                exp < 1000 -> 1000
                exp < 2000 -> 2000
                exp < 5000 -> 5000
                else -> 10000
            }
            val progress = (exp.toFloat() / maxExp.toFloat()).coerceIn(0f, 1f)

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Cấp VIP: $exp / $maxExp EXP",
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                    Text(
                        text = "Nạp thêm để lên VIP tiếp!",
                        fontSize = 10.sp,
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = GoldPrimary,
                    trackColor = Color.DarkGray
                )
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(text = label, fontSize = 9.sp, color = Color.Gray)
            Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

private fun getVipTitle(level: Int): String {
    return when (level) {
        0 -> "Dân Thường Coi Thường Rules"
        1 -> "Tập Sự Nạp"
        2 -> "Tay Chơi Xóm"
        3 -> "Dân Chơi VIP"
        5 -> "Trùm Luyện Kim"
        10 -> "Thánh Cổ Phần"
        99 -> "Đại Gia Vũ Trụ"
        else -> "Chúa Tể Cống Nộp"
    }
}
