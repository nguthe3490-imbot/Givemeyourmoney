package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGold
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.VipPurple
import java.text.NumberFormat
import java.util.Locale

/**
 * Minimalist Top Balance Bar component.
 * Displays player virtual currency (Gold, Gems, VIP status) in a sleek, non-cluttered M3 minimalist bar.
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MinimalistBalanceBar(
    gold: Long,
    gems: Long,
    vipLevel: Int,
    vipExp: Int,
    totalSpentVnd: Long,
    onOpenShop: () -> Unit,
    onClaimDailyGold: () -> Unit,
    onExchangeGemsToGold: (gemsToConvert: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = remember { NumberFormat.getNumberInstance(Locale("vi", "VN")) }
    var showQuickActionSheet by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("minimalist_balance_bar"),
        color = Color(0xFF181224),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Main Minimalist Currency Bar Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gold Pill
                CurrencyPill(
                    icon = Icons.Default.MonetizationOn,
                    iconTint = GoldPrimary,
                    valueText = currencyFormat.format(gold),
                    label = "Vàng Ảo",
                    borderColor = GoldPrimary.copy(alpha = 0.35f),
                    backgroundColor = Color(0xFF231A0C),
                    onAddClick = { showQuickActionSheet = true },
                    testTag = "balance_pill_gold"
                )

                // Gems Pill
                CurrencyPill(
                    icon = Icons.Default.Star,
                    iconTint = DiamondCyan,
                    valueText = currencyFormat.format(gems),
                    label = "Ngọc Nạp",
                    borderColor = DiamondCyan.copy(alpha = 0.35f),
                    backgroundColor = Color(0xFF0C202B),
                    onAddClick = onOpenShop,
                    testTag = "balance_pill_gems"
                )

                // VIP Chip
                VipLevelPill(
                    vipLevel = vipLevel,
                    vipExp = vipExp,
                    onClick = { showQuickActionSheet = true },
                    testTag = "balance_pill_vip"
                )
            }
        }
    }

    // Quick Gold & Exchange Bottom Sheet Modal
    if (showQuickActionSheet) {
        ModalBottomSheet(
            onDismissRequest = { showQuickActionSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color(0xFF1E152A)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💰 QUẢN LÝ TÀI CHÍNH VÀNG ẢO",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = GoldPrimary
                )
                Text(
                    text = "Nhận vàng miễn phí hoặc đổi Ngọc Nạp sang Vàng Ảo",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Action 1: Free Daily Gold
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            onClaimDailyGold()
                            showQuickActionSheet = false
                        }
                        .testTag("action_claim_daily_gold"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1C3B))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(GoldPrimary.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = "Daily Gold",
                                tint = GoldPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Điểm Danh Nhận +1,000 Vàng",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "NPH bố đức cho người chơi chăm chỉ",
                                fontSize = 11.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                }

                // Action 2: Convert Gems to Gold
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            onExchangeGemsToGold(50)
                            showQuickActionSheet = false
                        }
                        .testTag("action_convert_gems_gold"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A1C3B))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(DiamondCyan.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Convert Gems",
                                tint = DiamondCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Đổi 50 Ngọc -> +5,000 Vàng Ảo",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Tỷ lệ hời nhất vũ trụ Pay2Win",
                                fontSize = 11.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { showQuickActionSheet = false },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Đóng", color = Color.Gray)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CurrencyPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    valueText: String,
    label: String,
    borderColor: Color,
    backgroundColor: Color,
    onAddClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))

        // Animated Value Transition for visual polish
        AnimatedContent(
            targetState = valueText,
            transitionSpec = {
                (slideInVertically { height -> height } + fadeIn()) togetherWith
                        (slideOutVertically { height -> -height } + fadeOut())
            },
            label = "currency_anim"
        ) { text ->
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Plus Button for Quick Action
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.25f))
                .clickable { onAddClick() }
                .testTag("${testTag}_add"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add $label",
                tint = iconTint,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun VipLevelPill(
    vipLevel: Int,
    vipExp: Int,
    onClick: () -> Unit,
    testTag: String
) {
    val maxExp = remember(vipExp) {
        when {
            vipExp < 50 -> 50
            vipExp < 200 -> 200
            vipExp < 500 -> 500
            vipExp < 1000 -> 1000
            vipExp < 2000 -> 2000
            vipExp < 5000 -> 5000
            else -> 10000
        }
    }
    val progress = (vipExp.toFloat() / maxExp.toFloat()).coerceIn(0f, 1f)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF2A153A))
            .border(1.dp, VipPurple.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.WorkspacePremium,
            contentDescription = "VIP",
            tint = GoldPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))

        Column {
            Text(
                text = "VIP $vipLevel",
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                color = GoldPrimary
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(36.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = GoldPrimary,
                trackColor = Color.DarkGray
            )
        }
    }
}
