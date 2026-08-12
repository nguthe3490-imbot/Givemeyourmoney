package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BrokeMockPopupModal
import com.example.ui.components.CertificateDialog
import com.example.ui.components.MinimalistBalanceBar
import com.example.ui.components.MoneyParticleOverlay
import com.example.ui.components.PayPopupModal
import com.example.ui.components.VipBanner
import com.example.ui.screens.GachaScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MainGameScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.theme.DarkGold
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.HayDuaTienChoToiTheme
import com.example.ui.viewmodel.GameViewModel
import com.example.ui.viewmodel.SatiricalPopup
import com.example.util.SatiricalTtsManager

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HayDuaTienChoToiTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: GameViewModel) {
    val context = LocalContext.current
    val ttsManager = remember { SatiricalTtsManager(context) }

    DisposableEffect(Unit) {
        onDispose {
            ttsManager.shutdown()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.pvpSpeechEvent.collect { speechText ->
            ttsManager.speak(speechText)
        }
    }

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val purchaseHistory by viewModel.purchaseHistory.collectAsStateWithLifecycle()
    val activePopup by viewModel.activePopup.collectAsStateWithLifecycle()
    val monsterHp by viewModel.monsterHp.collectAsStateWithLifecycle()
    val monsterMaxHp by viewModel.monsterMaxHp.collectAsStateWithLifecycle()
    val systemNotice by viewModel.systemNotice.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val showCertificate by viewModel.showCertificate.collectAsStateWithLifecycle()

    // PvP state flows
    val pvpModeSelected by viewModel.pvpModeSelected.collectAsStateWithLifecycle()
    val pvpPlayerHp by viewModel.pvpPlayerHp.collectAsStateWithLifecycle()
    val pvpPlayerMaxHp by viewModel.pvpPlayerMaxHp.collectAsStateWithLifecycle()
    val pvpPlayerMana by viewModel.pvpPlayerMana.collectAsStateWithLifecycle()
    val pvpPlayerPower by viewModel.pvpPlayerPower.collectAsStateWithLifecycle()
    val pvpOpponentHp by viewModel.pvpOpponentHp.collectAsStateWithLifecycle()
    val pvpOpponentMaxHp by viewModel.pvpOpponentMaxHp.collectAsStateWithLifecycle()
    val pvpOpponentName by viewModel.pvpOpponentName.collectAsStateWithLifecycle()
    val pvpOpponentPower by viewModel.pvpOpponentPower.collectAsStateWithLifecycle()
    val pvpStance by viewModel.pvpStance.collectAsStateWithLifecycle()
    val pvpComboStep by viewModel.pvpComboStep.collectAsStateWithLifecycle()
    val pvpGodModeTime by viewModel.pvpGodModeTime.collectAsStateWithLifecycle()
    val pvpSkill1Cd by viewModel.pvpSkill1Cd.collectAsStateWithLifecycle()
    val pvpSkill2Cd by viewModel.pvpSkill2Cd.collectAsStateWithLifecycle()
    val pvpSkill3Cd by viewModel.pvpSkill3Cd.collectAsStateWithLifecycle()
    val pvpBattleLogs by viewModel.pvpBattleLogs.collectAsStateWithLifecycle()
    val pvpAiSpeechText by viewModel.pvpAiSpeechText.collectAsStateWithLifecycle()
    val pvpAiIsSpeaking by viewModel.pvpAiIsSpeaking.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFF120C1B),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1B1128),
                contentColor = GoldPrimary,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = { Icon(imageVector = Icons.Default.SportsKabaddi, contentDescription = "Game") },
                    label = { Text(text = "Đấu Trường", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("tab_game")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = { Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = "Shop") },
                    label = { Text(text = "Cửa Hàng", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("tab_shop")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = { Icon(imageVector = Icons.Default.Casino, contentDescription = "Gacha") },
                    label = { Text(text = "Vòng Quay", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("tab_gacha")
                )

                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    icon = { Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = "Rank") },
                    label = { Text(text = "Đại Gia", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("tab_rank")
                )

                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { viewModel.selectTab(4) },
                    icon = { Icon(imageVector = Icons.Default.Receipt, contentDescription = "History") },
                    label = { Text(text = "Nhật Ký", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = GoldPrimary,
                        indicatorColor = GoldPrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("tab_history")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Minimalist Top Balance Bar (Virtual Currency Manager & VIP)
            MinimalistBalanceBar(
                gold = userProfile?.gold ?: 100,
                gems = userProfile?.gems ?: 0,
                vipLevel = userProfile?.vipLevel ?: 0,
                vipExp = userProfile?.vipExp ?: 0,
                totalSpentVnd = userProfile?.totalSpentVnd ?: 0,
                onOpenShop = { viewModel.selectTab(1) },
                onClaimDailyGold = { viewModel.claimDailyGold() },
                onExchangeGemsToGold = { gemsToConvert -> viewModel.exchangeGemsToGold(gemsToConvert) }
            )

            // Content Area according to selected tab
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> MainGameScreen(
                        user = userProfile,
                        monsterHp = monsterHp,
                        monsterMaxHp = monsterMaxHp,
                        systemNotice = systemNotice,
                        pvpModeSelected = pvpModeSelected,
                        onTogglePvpMode = { selected -> viewModel.setPvpModeSelected(selected) },
                        pvpPlayerHp = pvpPlayerHp,
                        pvpPlayerMaxHp = pvpPlayerMaxHp,
                        pvpPlayerMana = pvpPlayerMana,
                        pvpPlayerPower = pvpPlayerPower,
                        pvpOpponentHp = pvpOpponentHp,
                        pvpOpponentMaxHp = pvpOpponentMaxHp,
                        pvpOpponentName = pvpOpponentName,
                        pvpOpponentPower = pvpOpponentPower,
                        pvpStance = pvpStance,
                        pvpComboStep = pvpComboStep,
                        pvpGodModeTime = pvpGodModeTime,
                        pvpSkill1Cd = pvpSkill1Cd,
                        pvpSkill2Cd = pvpSkill2Cd,
                        pvpSkill3Cd = pvpSkill3Cd,
                        pvpBattleLogs = pvpBattleLogs,
                        pvpAiSpeechText = pvpAiSpeechText,
                        pvpAiIsSpeaking = pvpAiIsSpeaking,
                        onTriggerPvpAiSatire = { viewModel.triggerGeminiPvpSatire() },
                        onUsePvpSkill = { idx -> viewModel.executePvpSkill(idx) },
                        onChangePvpStance = { stance -> viewModel.changePvpStance(stance) },
                        onBuyPvpPowerBoost = { viewModel.buyPvpPowerBoost() },
                        onBuyPvpGodShield = { viewModel.buyPvpGodShield() },
                        onBuyPvpInstantHeal = { viewModel.buyPvpInstantHeal() },
                        onBuyPvpResetCd = { viewModel.buyPvpResetCd() },
                        onBuyPvpOneShot = { viewModel.buyPvpOneShot() },
                        onFindNewPvpOpponent = { viewModel.findNewPvpOpponent() },
                        onAttackClick = { viewModel.onAttackClick() },
                        onMoveClick = { viewModel.onMoveClick() },
                        onAutoClick = { viewModel.onAutoClick() },
                        onSoundClick = { viewModel.onSoundClick() },
                        onSettingsClick = { viewModel.onSettingsClick() },
                        onRefillOxyClick = { viewModel.refillOxy() },
                        onPayToBreatheClick = { viewModel.payToBreathe() },
                        onOpenCertificateClick = { viewModel.setShowCertificate(true) }
                    )

                    1 -> ShopScreen(
                        onBuyPackage = { pkg ->
                            viewModel.processPurchase(
                                packageName = pkg.name,
                                amountVnd = pkg.priceVnd,
                                gemsGranted = pkg.gems,
                                vipExpGranted = pkg.vipExp,
                                paymentMethod = "MoMo Mô Phỏng",
                                comment = "Nạp gói ${pkg.name}",
                                unlockType = pkg.unlockType
                            )
                        }
                    )

                    2 -> GachaScreen(
                        user = userProfile,
                        systemNotice = systemNotice,
                        onSpinClick = { viewModel.spinGacha() },
                        onSpinWithGoldClick = { viewModel.spinGachaWithGold() }
                    )

                    3 -> LeaderboardScreen(user = userProfile)

                    4 -> HistoryScreen(
                        user = userProfile,
                        purchases = purchaseHistory,
                        onOpenCertificateClick = { viewModel.setShowCertificate(true) },
                        onResetDataClick = { viewModel.resetData() }
                    )
                }
            }
        }
    }

    // Active Satirical Popup Dialog
    activePopup?.let { popup ->
        when (popup) {
            is SatiricalPopup.RequiredPaywall -> {
                PayPopupModal(
                    title = popup.title,
                    message = popup.message,
                    priceVnd = popup.priceVnd,
                    actionName = popup.actionName,
                    onDismiss = { viewModel.dismissPopup() },
                    onConfirmPay = { method ->
                        viewModel.processPurchase(
                            packageName = "Kích Hoạt ${popup.title}",
                            amountVnd = popup.priceVnd,
                            gemsGranted = (popup.priceVnd / 100),
                            vipExpGranted = (popup.priceVnd / 200).toInt(),
                            paymentMethod = method,
                            comment = "Cống nộp mở khóa tính năng",
                            unlockType = popup.unlockType
                        )
                    }
                )
            }

            is SatiricalPopup.FlashSale -> {
                PayPopupModal(
                    title = popup.title,
                    message = "⚡ Gói Khuyến Mãi Đột Xuất: ${popup.discountText}! Đưa tiền ngay kẻo hết hạn!",
                    priceVnd = popup.priceVnd,
                    actionName = "Cống Nộp Nhanh",
                    onDismiss = { viewModel.dismissPopup() },
                    onConfirmPay = { method ->
                        viewModel.processPurchase(
                            packageName = popup.title,
                            amountVnd = popup.priceVnd,
                            gemsGranted = popup.rewardGems,
                            vipExpGranted = (popup.priceVnd / 200).toInt(),
                            paymentMethod = method,
                            comment = "Mua Flash Sale Hút Máu"
                        )
                    }
                )
            }

            is SatiricalPopup.OxyWarning -> {
                PayPopupModal(
                    title = "🫁 CẢNH BÁO OXY CỰC NGUY CẤP!",
                    message = "Nhân vật của bạn đang ngạt thở! NPH yêu cầu nạp 50.000đ để tiếp tục thở!",
                    priceVnd = popup.priceVnd,
                    actionName = "Nạp Oxy Sinh Tồn",
                    onDismiss = { viewModel.dismissPopup() },
                    onConfirmPay = { method ->
                        viewModel.refillOxy()
                    }
                )
            }

            is SatiricalPopup.CustomAlert -> {
                PayPopupModal(
                    title = popup.title,
                    message = popup.message,
                    priceVnd = popup.priceVnd,
                    actionName = "Nạp Tiền Ngay",
                    onDismiss = { viewModel.dismissPopup() },
                    onConfirmPay = { method ->
                        viewModel.processPurchase(
                            packageName = "Nạp Bổ Sung",
                            amountVnd = popup.priceVnd,
                            gemsGranted = (popup.priceVnd / 100),
                            vipExpGranted = 50,
                            paymentMethod = method,
                            comment = "Nạp ngọc cấp tốc"
                        )
                    }
                )
            }

            is SatiricalPopup.BrokeAlert -> {
                BrokeMockPopupModal(
                    requiredGold = popup.requiredGold,
                    currentGold = popup.currentGold,
                    gemsCount = userProfile?.gems ?: 0,
                    onDismiss = { viewModel.dismissPopup() },
                    onBuyReliefPackage = {
                        viewModel.processPurchase(
                            packageName = "Gói Cứu Trợ 'Chống Nghèo' (10k)",
                            amountVnd = 10000,
                            gemsGranted = 100,
                            vipExpGranted = 50,
                            paymentMethod = "MoMo Mô Phỏng",
                            comment = "Cống nộp cứu trợ nghèo khó"
                        )
                        viewModel.addGold(100000)
                    },
                    onExchangeGems = {
                        viewModel.exchangeGemsToGold(50)
                        viewModel.dismissPopup()
                    },
                    onClaimDailyGold = {
                        viewModel.claimDailyGold()
                        viewModel.dismissPopup()
                    },
                    onOpenShop = {
                        viewModel.dismissPopup()
                        viewModel.selectTab(1)
                    }
                )
            }
        }
    }

    // Certificate Dialog
    if (showCertificate) {
        CertificateDialog(
            user = userProfile,
            onDismiss = { viewModel.setShowCertificate(false) }
        )
    }

    // Money Rain Particle Overlay (falling coins / USD animation)
    MoneyParticleOverlay(moneyRainEvent = viewModel.moneyRainEvent)
    }
}
