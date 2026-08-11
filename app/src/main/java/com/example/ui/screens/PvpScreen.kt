package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.ui.theme.DarkGold
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent
import com.example.ui.theme.VipPurple

@Composable
fun PvpScreen(
    user: UserEntity?,
    playerHp: Int,
    playerMaxHp: Int,
    playerMana: Int,
    playerPower: Long,
    opponentHp: Int,
    opponentMaxHp: Int,
    opponentName: String,
    opponentPower: Long,
    stance: String,
    comboStep: Int,
    godModeTime: Int,
    skill1Cd: Int,
    skill2Cd: Int,
    skill3Cd: Int,
    battleLogs: List<String>,
    aiSpeechText: String = "🎙️ AI đang rình bạn chần chừ không nạp tiền...",
    aiIsSpeaking: Boolean = false,
    onTriggerAiSatire: () -> Unit = {},
    onUseSkill: (Int) -> Unit,
    onChangeStance: (String) -> Unit,
    onBuyPowerBoost: () -> Unit,
    onBuyGodShield: () -> Unit,
    onBuyInstantHeal: () -> Unit,
    onBuyResetCd: () -> Unit,
    onBuyOneShot: () -> Unit,
    onFindNewOpponent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D0814))
            .padding(10.dp)
    ) {
        // Mode Header & Matchmaking
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Brush.linearGradient(listOf(NeonRedAccent, VipPurple)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsKabaddi,
                        contentDescription = "PVP",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "⚔️ ĐẤU TRƯỜNG PVP KỸ NĂNG CAO",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldPrimary
                    )
                    Text(
                        text = "Cơ chế rối rắm - Nạp tiền là ăn ngay!",
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFF241535), RoundedCornerShape(12.dp))
                    .border(1.dp, GoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .clickable { onFindNewOpponent() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("pvp_find_opponent_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = GoldPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Đổi Đối Thủ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Versus Arena Battle Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GoldPrimary, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1128))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                // Players VS Banner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Player
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🧑‍💻 BẠN (VIP ${user?.vipLevel ?: 0})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = EmeraldGreen
                            )
                            if (godModeTime > 0) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .background(GoldPrimary, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("BẤT TỬ ${godModeTime}s", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.Black)
                                }
                            }
                        }
                        Text(
                            text = "Lực chiến: ${playerPower} Dmg",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        LinearProgressIndicator(
                            progress = { playerHp.toFloat() / playerMaxHp.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = EmeraldGreen,
                            trackColor = Color.DarkGray
                        )
                        Text(
                            text = "HP: $playerHp / $playerMaxHp | MP: $playerMana",
                            fontSize = 9.sp,
                            color = Color.LightGray
                        )
                    }

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(30.dp)
                            .background(NeonRedAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("VS", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }

                    // Right Opponent
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = opponentName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonRedAccent,
                            textAlign = TextAlign.End
                        )
                        Text(
                            text = "Lực chiến: ${opponentPower} Dmg",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        LinearProgressIndicator(
                            progress = { opponentHp.toFloat() / opponentMaxHp.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = NeonRedAccent,
                            trackColor = Color.DarkGray
                        )
                        Text(
                            text = "HP: $opponentHp / $opponentMaxHp",
                            fontSize = 9.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stance Selector & Combo Status Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF26183B), RoundedCornerShape(10.dp))
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Thế Trận (Stance):", fontSize = 9.sp, color = Color.Gray)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            StanceChip("ATTACK", "🗡️ Công", stance == "ATTACK") { onChangeStance("ATTACK") }
                            StanceChip("DEFENSE", "🛡️ Thủ", stance == "DEFENSE") { onChangeStance("DEFENSE") }
                            StanceChip("COUNTER", "⚡ Phản", stance == "COUNTER") { onChangeStance("COUNTER") }
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Chuỗi Combo Kỹ Năng:", fontSize = 9.sp, color = Color.Gray)
                        Text(
                            text = when (comboStep) {
                                1 -> "1/3: 🗡️ Đao Pháp"
                                2 -> "2/3: 🦶 Cước Pháp"
                                3 -> "🔥 3/3: THẦN LONG TRƯỞNG!"
                                else -> "Chưa kích hoạt Combo"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = DiamondCyan
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Gemini AI Satirical Voice Commentary Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GoldPrimary, RoundedCornerShape(12.dp))
                .clickable { onTriggerAiSatire() }
                .testTag("pvp_trigger_ai_satire_banner"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2B163B))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Brush.radialGradient(listOf(GoldPrimary, VipPurple)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Voice AI",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🎙️ AI CHÂM BIẾM NẠP TIỀN (GEMINI 1.5)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldPrimary
                            )
                            if (aiIsSpeaking) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "⚡ Đang nghĩ...",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DiamondCyan
                                )
                            }
                        }
                        Text(
                            text = "\"$aiSpeechText\"",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 14.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(GoldPrimary, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🔊 Phát AI",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // P2W Instant Power Boost Shop Bar
        Text(
            text = "🚀 TĂNG SỨC MẠNH TỨC THÌ (P2W FAST-FORWARD):",
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            P2WBoostButton(
                title = "+50k Lực Chiến",
                price = "500 Vàng",
                icon = Icons.Default.FlashOn,
                color = GoldPrimary,
                onClick = onBuyPowerBoost,
                modifier = Modifier.weight(1f),
                testTag = "pvp_boost_power"
            )
            P2WBoostButton(
                title = "Bất Tử 10s",
                price = "1.000 Vàng",
                icon = Icons.Default.Shield,
                color = VipPurple,
                onClick = onBuyGodShield,
                modifier = Modifier.weight(1f),
                testTag = "pvp_boost_shield"
            )
            P2WBoostButton(
                title = "Hồi 100% HP",
                price = "300 Vàng",
                icon = Icons.Default.LocalFireDepartment,
                color = EmeraldGreen,
                onClick = onBuyInstantHeal,
                modifier = Modifier.weight(1f),
                testTag = "pvp_boost_heal"
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            P2WBoostButton(
                title = "Xóa Cooldown",
                price = "200 Vàng",
                icon = Icons.Default.Bolt,
                color = DiamondCyan,
                onClick = onBuyResetCd,
                modifier = Modifier.weight(1f),
                testTag = "pvp_boost_cd"
            )
            P2WBoostButton(
                title = "💣 ONE-SHOT KO",
                price = "50.000 Vàng",
                icon = Icons.Default.WorkspacePremium,
                color = NeonRedAccent,
                onClick = onBuyOneShot,
                modifier = Modifier.weight(1f),
                testTag = "pvp_boost_oneshot"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Complex Skill Buttons
        Text(
            text = "🎯 KỸ NĂNG CHIẾN ĐẤU (Bấm đúng chuỗi để tạo Combo):",
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SkillButton(
                title = "1. Đao Pháp",
                cd = skill1Cd,
                onClick = { onUseSkill(1) },
                modifier = Modifier.weight(1f),
                testTag = "pvp_skill_1"
            )
            SkillButton(
                title = "2. Cước Pháp",
                cd = skill2Cd,
                onClick = { onUseSkill(2) },
                modifier = Modifier.weight(1f),
                testTag = "pvp_skill_2"
            )
            SkillButton(
                title = "3. Chưởng Pháp",
                cd = skill3Cd,
                onClick = { onUseSkill(3) },
                modifier = Modifier.weight(1f),
                testTag = "pvp_skill_3"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable Battle Log
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(1.dp, Color(0xFF332348), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF140D21)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "📜 NHẬT KÝ DIỄN BIẾN TRẬN ĐẤU (PVP REAL-TIME):",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    reverseLayout = true
                ) {
                    items(battleLogs.reversed()) { log ->
                        Text(
                            text = log,
                            fontSize = 10.sp,
                            color = if (log.contains("NẠP") || log.contains("MẠNH")) GoldPrimary else Color.LightGray,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StanceChip(
    stanceKey: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) GoldPrimary else Color(0xFF3B2754),
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color.White
        )
    }
}

@Composable
private fun P2WBoostButton(
    title: String,
    price: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Box(
        modifier = modifier
            .background(Color(0xFF221633), RoundedCornerShape(10.dp))
            .border(1.dp, color, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = price,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
private fun SkillButton(
    title: String,
    cd: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Button(
        onClick = onClick,
        enabled = cd <= 0,
        modifier = modifier
            .height(42.dp)
            .testTag(testTag),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3D235C),
            disabledContainerColor = Color(0xFF1E142B),
            contentColor = Color.White,
            disabledContentColor = Color.Gray
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = if (cd > 0) "$title (${cd}s)" else title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
