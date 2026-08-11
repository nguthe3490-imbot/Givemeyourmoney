package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserEntity
import com.example.ui.components.OxyBar
import com.example.ui.theme.DarkGold
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent
import com.example.ui.theme.VipPurple

@Composable
fun MainGameScreen(
    user: UserEntity?,
    monsterHp: Int,
    monsterMaxHp: Int,
    systemNotice: String,
    pvpModeSelected: Boolean = false,
    onTogglePvpMode: (Boolean) -> Unit = {},
    // PvP State
    pvpPlayerHp: Int = 1000,
    pvpPlayerMaxHp: Int = 1000,
    pvpPlayerMana: Int = 100,
    pvpPlayerPower: Long = 500L,
    pvpOpponentHp: Int = 50000,
    pvpOpponentMaxHp: Int = 50000,
    pvpOpponentName: String = "👑 Cậu Ấm Nạp VIP15",
    pvpOpponentPower: Long = 25000L,
    pvpStance: String = "ATTACK",
    pvpComboStep: Int = 0,
    pvpGodModeTime: Int = 0,
    pvpSkill1Cd: Int = 0,
    pvpSkill2Cd: Int = 0,
    pvpSkill3Cd: Int = 0,
    pvpBattleLogs: List<String> = emptyList(),
    pvpAiSpeechText: String = "🎙️ AI đang rình bạn chần chừ không nạp tiền...",
    pvpAiIsSpeaking: Boolean = false,
    onTriggerPvpAiSatire: () -> Unit = {},
    onUsePvpSkill: (Int) -> Unit = {},
    onChangePvpStance: (String) -> Unit = {},
    onBuyPvpPowerBoost: () -> Unit = {},
    onBuyPvpGodShield: () -> Unit = {},
    onBuyPvpInstantHeal: () -> Unit = {},
    onBuyPvpResetCd: () -> Unit = {},
    onBuyPvpOneShot: () -> Unit = {},
    onFindNewPvpOpponent: () -> Unit = {},
    // Controls
    onAttackClick: () -> Unit,
    onMoveClick: () -> Unit,
    onAutoClick: () -> Unit,
    onSoundClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onRefillOxyClick: () -> Unit,
    onPayToBreatheClick: () -> Unit,
    onOpenCertificateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF120C1B))
            .padding(10.dp)
    ) {
        // Oxy & Life Support Bar Status
        OxyBar(
            oxyLevel = user?.oxyLevel ?: 100,
            canBreatheForever = user?.canBreatheForever ?: false,
            onRefillOxyClick = onRefillOxyClick,
            onPayToBreatheClick = onPayToBreatheClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mode Selector: PvE Slime vs PvP Kỹ Năng Cao
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E142B), RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFF38274C), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (!pvpModeSelected) GoldPrimary else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { onTogglePvpMode(false) }
                    .padding(vertical = 6.dp)
                    .testTag("mode_pve_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👾 Săn Slime PvE",
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = if (!pvpModeSelected) Color.Black else Color.White
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (pvpModeSelected) NeonRedAccent else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { onTogglePvpMode(true) }
                    .padding(vertical = 6.dp)
                    .testTag("mode_pvp_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⚔️ PvP Kỹ Năng Cao (P2W)",
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (pvpModeSelected) {
            PvpScreen(
                user = user,
                playerHp = pvpPlayerHp,
                playerMaxHp = pvpPlayerMaxHp,
                playerMana = pvpPlayerMana,
                playerPower = pvpPlayerPower,
                opponentHp = pvpOpponentHp,
                opponentMaxHp = pvpOpponentMaxHp,
                opponentName = pvpOpponentName,
                opponentPower = pvpOpponentPower,
                stance = pvpStance,
                comboStep = pvpComboStep,
                godModeTime = pvpGodModeTime,
                skill1Cd = pvpSkill1Cd,
                skill2Cd = pvpSkill2Cd,
                skill3Cd = pvpSkill3Cd,
                battleLogs = pvpBattleLogs,
                aiSpeechText = pvpAiSpeechText,
                aiIsSpeaking = pvpAiIsSpeaking,
                onTriggerAiSatire = onTriggerPvpAiSatire,
                onUseSkill = onUsePvpSkill,
                onChangeStance = onChangePvpStance,
                onBuyPowerBoost = onBuyPvpPowerBoost,
                onBuyGodShield = onBuyPvpGodShield,
                onBuyInstantHeal = onBuyPvpInstantHeal,
                onBuyResetCd = onBuyPvpResetCd,
                onBuyOneShot = onBuyPvpOneShot,
                onFindNewOpponent = onFindNewPvpOpponent,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Hero & Slime Battle Arena Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(1.5.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E152A))
            ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Hero Banner Background Image
                Image(
                    painter = painterResource(id = R.drawable.img_p2w_hero_1786098297975),
                    contentDescription = "Game Arena Hero",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.35f
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top System Chat Marquee Notice
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                            .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "📢 $systemNotice",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Monster / Slime Target Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF2A1B3B).copy(alpha = 0.85f), RoundedCornerShape(12.dp))
                            .border(1.dp, NeonRedAccent, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "👾 Slime Vô Địch (Cần Nạp $99 Để Giết)",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "$monsterHp / $monsterMaxHp HP",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = NeonRedAccent
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            LinearProgressIndicator(
                                progress = { monsterHp.toFloat() / monsterMaxHp.toFloat() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                color = NeonRedAccent,
                                trackColor = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "💬 \"Dùng tay đánh dở lắm! Nạp 10k mua Kiếm VIP đi bro!\"",
                                fontSize = 11.sp,
                                color = Color.LightGray,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }

                    // Certificate Button
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(listOf(DarkGold, VipPurple)),
                                RoundedCornerShape(20.dp)
                            )
                            .border(1.dp, GoldPrimary, RoundedCornerShape(20.dp))
                            .clickable { onOpenCertificateClick() }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("open_certificate_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Cert",
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Xem Giấy Chứng Nhận Nô Lệ VIP",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Paywalled Game Controls Title
        Text(
            text = "⚡ TÍNH NĂNG GAME (MỖI NÚT CẦN NẠP TIỀN BẢN QUYỀN):",
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = GoldPrimary,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Grid of Locked/Unlocked Buttons
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(180.dp)
        ) {
            item {
                GameActionButton(
                    title = "Chém Slime",
                    priceLabel = "10.000đ",
                    isUnlocked = (user?.canAttack == true || (user?.vipLevel ?: 0) >= 1),
                    icon = Icons.Default.SportsKabaddi,
                    onClick = onAttackClick,
                    testTag = "action_attack"
                )
            }

            item {
                GameActionButton(
                    title = "Di Chuyển Phải",
                    priceLabel = "20.000đ",
                    isUnlocked = (user?.canMove == true || (user?.vipLevel ?: 0) >= 2),
                    icon = Icons.Default.DirectionsRun,
                    onClick = onMoveClick,
                    testTag = "action_move"
                )
            }

            item {
                GameActionButton(
                    title = "Auto Treo Máy",
                    priceLabel = "VIP 5 (100k)",
                    isUnlocked = (user?.canAuto == true || (user?.vipLevel ?: 0) >= 5),
                    icon = Icons.Default.AutoAwesome,
                    onClick = onAutoClick,
                    testTag = "action_auto"
                )
            }

            item {
                GameActionButton(
                    title = "Bật Âm Thanh",
                    priceLabel = "15.000đ",
                    isUnlocked = (user?.canSound == true || (user?.vipLevel ?: 0) >= 1),
                    icon = Icons.Default.VolumeUp,
                    onClick = onSoundClick,
                    testTag = "action_sound"
                )
            }

            item {
                GameActionButton(
                    title = "Pause / Cài Đặt",
                    priceLabel = "50.000đ",
                    isUnlocked = (user?.canSettings == true),
                    icon = Icons.Default.Settings,
                    onClick = onSettingsClick,
                    testTag = "action_settings"
                )
            }

            item {
                GameActionButton(
                    title = "Gói Thượng Đế VIP999",
                    priceLabel = "10.000.000đ",
                    isUnlocked = (user?.isVip999God == true),
                    icon = Icons.Default.Pause,
                    onClick = onSettingsClick,
                    testTag = "action_god"
                )
            }
        }
    }
}
}

@Composable
private fun GameActionButton(
    title: String,
    priceLabel: String,
    isUnlocked: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(
                if (isUnlocked) Color(0xFF234B28) else Color(0xFF2B1A24),
                RoundedCornerShape(10.dp)
            )
            .border(
                1.dp,
                if (isUnlocked) Color(0xFF00C853) else NeonRedAccent,
                RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isUnlocked) Color(0xFF00E676) else Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 11.sp
                    )
                    Text(
                        text = if (isUnlocked) "ĐÃ MỞ KHÓA" else "Cần $priceLabel",
                        fontSize = 9.sp,
                        color = if (isUnlocked) Color(0xFFB9F6CA) else NeonRedAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Icon(
                imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                contentDescription = "Lock State",
                tint = if (isUnlocked) Color(0xFF00E676) else NeonRedAccent,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
