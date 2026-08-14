package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiSatireService
import com.example.data.AppDatabase
import com.example.data.GameRepository
import com.example.data.PurchaseEntity
import com.example.data.UserEntity
import com.example.util.MoneySoundType
import com.example.util.PaySoundEffects
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class SatiricalPopup {
    data class RequiredPaywall(val title: String, val message: String, val priceVnd: Long, val actionName: String, val unlockType: String) : SatiricalPopup()
    data class FlashSale(val title: String, val discountText: String, val priceVnd: Long, val rewardGems: Long) : SatiricalPopup()
    data class OxyWarning(val priceVnd: Long = 50000) : SatiricalPopup()
    data class CustomAlert(val title: String, val message: String, val priceVnd: Long = 10000) : SatiricalPopup()
    data class BrokeAlert(val requiredGold: Long = 1000, val currentGold: Long = 0) : SatiricalPopup()
}

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GameRepository

    val userProfile: StateFlow<UserEntity?>
    val purchaseHistory: StateFlow<List<PurchaseEntity>>

    private val _activePopup = MutableStateFlow<SatiricalPopup?>(null)
    val activePopup: StateFlow<SatiricalPopup?> = _activePopup.asStateFlow()

    private val _monsterHp = MutableStateFlow(1000)
    val monsterHp: StateFlow<Int> = _monsterHp.asStateFlow()

    private val _monsterMaxHp = MutableStateFlow(1000)
    val monsterMaxHp: StateFlow<Int> = _monsterMaxHp.asStateFlow()

    private val _systemNotice = MutableStateFlow("CHÀO MỪNG ĐẾN VỚI GAME PAY2WIN VŨ TRỤ! NẠP CÀNG NHIỀU CÀNG MẠNH!")
    val systemNotice: StateFlow<String> = _systemNotice.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _showCertificate = MutableStateFlow(false)
    val showCertificate: StateFlow<Boolean> = _showCertificate.asStateFlow()

    // PvP Arena State
    private val _pvpModeSelected = MutableStateFlow(false) // false = Slime PvE, true = PvP
    val pvpModeSelected: StateFlow<Boolean> = _pvpModeSelected.asStateFlow()

    private val _pvpPlayerHp = MutableStateFlow(1000)
    val pvpPlayerHp: StateFlow<Int> = _pvpPlayerHp.asStateFlow()

    private val _pvpPlayerMaxHp = MutableStateFlow(1000)
    val pvpPlayerMaxHp: StateFlow<Int> = _pvpPlayerMaxHp.asStateFlow()

    private val _pvpPlayerMana = MutableStateFlow(100)
    val pvpPlayerMana: StateFlow<Int> = _pvpPlayerMana.asStateFlow()

    private val _pvpPlayerPower = MutableStateFlow(500L)
    val pvpPlayerPower: StateFlow<Long> = _pvpPlayerPower.asStateFlow()

    private val _pvpOpponentHp = MutableStateFlow(50000)
    val pvpOpponentHp: StateFlow<Int> = _pvpOpponentHp.asStateFlow()

    private val _pvpOpponentMaxHp = MutableStateFlow(50000)
    val pvpOpponentMaxHp: StateFlow<Int> = _pvpOpponentMaxHp.asStateFlow()

    private val _pvpOpponentName = MutableStateFlow("👑 Cậu Ấm Nạp VIP15")
    val pvpOpponentName: StateFlow<String> = _pvpOpponentName.asStateFlow()

    private val _pvpOpponentPower = MutableStateFlow(25000L)
    val pvpOpponentPower: StateFlow<Long> = _pvpOpponentPower.asStateFlow()

    private val _pvpStance = MutableStateFlow("ATTACK")
    val pvpStance: StateFlow<String> = _pvpStance.asStateFlow()

    private val _pvpComboStep = MutableStateFlow(0)
    val pvpComboStep: StateFlow<Int> = _pvpComboStep.asStateFlow()

    private val _pvpGodModeTime = MutableStateFlow(0)
    val pvpGodModeTime: StateFlow<Int> = _pvpGodModeTime.asStateFlow()

    private val _pvpSkill1Cd = MutableStateFlow(0)
    val pvpSkill1Cd: StateFlow<Int> = _pvpSkill1Cd.asStateFlow()

    private val _pvpSkill2Cd = MutableStateFlow(0)
    val pvpSkill2Cd: StateFlow<Int> = _pvpSkill2Cd.asStateFlow()

    private val _pvpSkill3Cd = MutableStateFlow(0)
    val pvpSkill3Cd: StateFlow<Int> = _pvpSkill3Cd.asStateFlow()

    private val _pvpBattleLogs = MutableStateFlow<List<String>>(
        listOf("⚔️ Đã kết nối Đấu Trường PvP Server! Đối thủ của bạn: '👑 Cậu Ấm Nạp VIP15'.")
    )
    val pvpBattleLogs: StateFlow<List<String>> = _pvpBattleLogs.asStateFlow()

    // Gemini AI Satirical Voice Commentary State
    private val _pvpAiSpeechText = MutableStateFlow("🎙️ AI đang rình bạn chần chừ không nạp tiền...")
    val pvpAiSpeechText: StateFlow<String> = _pvpAiSpeechText.asStateFlow()

    private val _pvpAiIsSpeaking = MutableStateFlow(false)
    val pvpAiIsSpeaking: StateFlow<Boolean> = _pvpAiIsSpeaking.asStateFlow()

    private val _pvpSpeechEvent = MutableSharedFlow<String>()
    val pvpSpeechEvent: SharedFlow<String> = _pvpSpeechEvent.asSharedFlow()

    // Particle system trigger event for falling money/USD
    private val _moneyRainEvent = MutableSharedFlow<Unit>()
    val moneyRainEvent: SharedFlow<Unit> = _moneyRainEvent.asSharedFlow()

    fun triggerMoneyRain() {
        viewModelScope.launch {
            _moneyRainEvent.emit(Unit)
        }
    }

    /**
     * Triggers procedural audio for 'ting ting' coin chime or money counter sound.
     */
    fun playPaySound(type: MoneySoundType = MoneySoundType.TING_TING) {
        PaySoundEffects.play(type)
    }

    private var oxyJob: Job? = null
    private var randomPopupJob: Job? = null
    private var pvpHesitationJob: Job? = null

    // Currency balance management
    fun addGold(amount: Long) {
        viewModelScope.launch {
            val user = userProfile.value ?: return@launch
            val updated = user.copy(gold = user.gold + amount)
            repository.updateUser(updated)
            _systemNotice.value = "Tài khoản vừa cộng +$amount Vàng Ảo!"
            _moneyRainEvent.emit(Unit)
            PaySoundEffects.play(MoneySoundType.TING_TING)
        }
    }

    fun addGems(amount: Long) {
        viewModelScope.launch {
            val user = userProfile.value ?: return@launch
            val updated = user.copy(gems = user.gems + amount)
            repository.updateUser(updated)
            _systemNotice.value = "Tài khoản vừa cộng +$amount Ngọc Nạp!"
            _moneyRainEvent.emit(Unit)
            PaySoundEffects.play(MoneySoundType.MONEY_COUNTER)
        }
    }

    fun triggerBrokePopup(requiredGold: Long = 1000) {
        val currentGold = userProfile.value?.gold ?: 0
        _activePopup.value = SatiricalPopup.BrokeAlert(requiredGold = requiredGold, currentGold = currentGold)
    }

    fun spendGold(amount: Long): Boolean {
        val user = userProfile.value ?: return false
        if (user.gold >= amount) {
            viewModelScope.launch {
                val updated = user.copy(gold = user.gold - amount)
                repository.updateUser(updated)
            }
            return true
        } else {
            _systemNotice.value = "Thất bại: Bạn cần $amount Vàng Ảo nhưng chỉ có ${user.gold} Vàng!"
            triggerBrokePopup(requiredGold = amount)
            return false
        }
    }

    fun spendGems(amount: Long): Boolean {
        val user = userProfile.value ?: return false
        if (user.gems >= amount) {
            viewModelScope.launch {
                val updated = user.copy(gems = user.gems - amount)
                repository.updateUser(updated)
            }
            return true
        } else {
            _activePopup.value = SatiricalPopup.CustomAlert(
                title = "💎 HẾT NGỌC NẠP!",
                message = "Cần $amount Ngọc Nạp! Hãy cống nộp thêm 20.000đ để tiếp tục!",
                priceVnd = 20000
            )
            return false
        }
    }

    fun claimDailyGold() {
        viewModelScope.launch {
            val user = userProfile.value ?: return@launch
            val updated = user.copy(gold = user.gold + 1000)
            repository.updateUser(updated)
            _systemNotice.value = "🎁 ĐIỂM DANH THÀNH CÔNG: Đã nhận +1,000 Vàng Ảo từ NPH!"
        }
    }

    fun exchangeGemsToGold(gemsToConvert: Long = 50) {
        viewModelScope.launch {
            val user = userProfile.value ?: return@launch
            if (user.gems >= gemsToConvert) {
                val goldGranted = gemsToConvert * 100
                val updated = user.copy(gems = user.gems - gemsToConvert, gold = user.gold + goldGranted)
                repository.updateUser(updated)
                _systemNotice.value = "💱 CHUYỂN ĐỔI THÀNH CÔNG: -$gemsToConvert Ngọc -> +$goldGranted Vàng Ảo!"
            } else {
                _systemNotice.value = "Không đủ Ngọc Nạp để đổi! Cần ít nhất $gemsToConvert Ngọc."
            }
        }
    }

    init {
        val db = AppDatabase.getInstance(application)
        repository = GameRepository(db.userDao(), db.purchaseDao())

        userProfile = repository.userProfile.toStateFlow(null)
        purchaseHistory = repository.allPurchases.toStateFlow(emptyList())

        viewModelScope.launch {
            repository.getOrCreateUser()
            startOxyTimer()
            startRandomPopupEngine()
            startPvpHesitationTimer()
        }
    }

    fun triggerGeminiPvpSatire(reason: String = "manual") {
        viewModelScope.launch {
            _pvpAiIsSpeaking.value = true
            val satireText = GeminiSatireService.generatePvpSatire(
                opponentName = _pvpOpponentName.value,
                playerPower = _pvpPlayerPower.value
            )
            _pvpAiSpeechText.value = satireText
            _pvpAiIsSpeaking.value = false

            // Emit speech event for TTS
            _pvpSpeechEvent.emit(satireText)

            val newLogs = _pvpBattleLogs.value.toMutableList()
            newLogs.add("🗣️ AI Châm Biếm Nạp Tiền: \"$satireText\"")
            _pvpBattleLogs.value = newLogs.takeLast(20)
        }
    }

    private fun startPvpHesitationTimer() {
        pvpHesitationJob?.cancel()
        pvpHesitationJob = viewModelScope.launch {
            var secondsInactive = 0
            while (true) {
                delay(1000)
                if (_pvpModeSelected.value) {
                    secondsInactive++
                    if (secondsInactive >= 10) {
                        secondsInactive = 0
                        triggerGeminiPvpSatire("hesitation_timer")
                    }
                } else {
                    secondsInactive = 0
                }
            }
        }
    }


    private fun <T> kotlinx.coroutines.flow.Flow<T>.toStateFlow(initial: T): StateFlow<T> {
        val flowState = MutableStateFlow(initial)
        viewModelScope.launch {
            this@toStateFlow.collectLatest { flowState.value = it }
        }
        return flowState.asStateFlow()
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
        incrementClickCount()
    }

    private fun incrementClickCount() {
        val user = userProfile.value ?: return
        // 20% chance on click to trigger random greedy pop-up if no popup open
        if (_activePopup.value == null && (1..100).random() <= 20) {
            triggerRandomFlashSale()
        }
    }

    fun dismissPopup() {
        viewModelScope.launch {
            val user = userProfile.value ?: return@launch
            val updated = user.copy(totalPopupsClosed = user.totalPopupsClosed + 1)
            repository.updateUser(updated)
            _activePopup.value = null
            _systemNotice.value = "NPH cảnh báo: Tắt popup sẽ bỏ lỡ cơ hội thành Đại Gia!"
        }
    }

    fun triggerRandomFlashSale() {
        val sales = listOf(
            SatiricalPopup.FlashSale("⚡ FLASH SALE 3 GIÂY", "Giảm giá 99.9% cho Gói Hút Máu!", 20000, 500),
            SatiricalPopup.FlashSale("💎 BẢO HIỂM THUẾ GAME", "Nạp để tránh bị NPH trừ ngọc vô lý!", 30000, 800),
            SatiricalPopup.FlashSale("🚀 GÓI BẮT CẦU VIP", "Tăng 0.0001% tỷ lệ trúng Gacha!", 50000, 1500),
            SatiricalPopup.FlashSale("👑 DANH HIỆU 'NẠP SƠN HÀ'", "Được toàn server gọi là Đại Gia!", 100000, 3000)
        )
        _activePopup.value = sales.random()
    }

    fun payToBreathe() {
        incrementClickCount()
        val user = userProfile.value ?: return
        val costGold = 500L

        if (user.canBreatheForever) {
            _systemNotice.value = "👑 VIP THƯỢNG ĐẾ: Bạn đã có 'Bảo Hiểm Oxy Vĩnh Cửu', hít thở hoàn toàn miễn phí!"
            return
        }

        if (user.gold >= costGold) {
            viewModelScope.launch {
                val newOxy = (user.oxyLevel + 25).coerceAtMost(100)
                val updated = user.copy(gold = user.gold - costGold, oxyLevel = newOxy)
                repository.updateUser(updated)

                val funnyMessages = listOf(
                    "🫁 ĐÃ NẠP TIỀN ĐỂ THỞ: Hít vào 1 hơi mất $costGold Vàng! NPH trân trọng gửi lời cảm ơn tới cuống phổi!",
                    "💸 PHỔI BẠN ĐÃ ĐƯỢC GIA HẠN! Phí duy trì tế bào: $costGold Vàng. Bạn vừa hít thở thêm 25% Oxy!",
                    "🌬️ NẠP TIỀN THỞ THÀNH CÔNG! NPH cấp phép cho bạn hít thở tiếp. Đừng quên nạp trước khi hết Oxy!",
                    "💰 THUẾ OXY ĐÃ THANH TOÁN: Tế bào O2 hoạt động trở lại! Bạn có thể tiếp tục thao tác game!"
                )
                _systemNotice.value = funnyMessages.random()
            }
        } else {
            _activePopup.value = SatiricalPopup.OxyWarning(priceVnd = 50000)
            _systemNotice.value = "😱 HẾT VÀNG ĐỂ THỞ! Cần $costGold Vàng Ảo hoặc Nạp 50.000đ để mở van Oxy lập tức!"
        }
    }

    private fun consumeOxyOnAction(user: UserEntity): Boolean {
        if (user.canBreatheForever) return true
        if (user.oxyLevel <= 0) {
            _systemNotice.value = "🫁 TẮT THỞ RỒI! Phổi hết Oxy, không thể thao tác! Nhấn 'Nạp tiền để tiếp tục thở' ngay!"
            _activePopup.value = SatiricalPopup.OxyWarning(priceVnd = 50000)
            return false
        }
        viewModelScope.launch {
            val updated = user.copy(oxyLevel = (user.oxyLevel - 5).coerceAtLeast(0))
            repository.updateUser(updated)
        }
        return true
    }

    fun onAttackClick() {
        incrementClickCount()
        val user = userProfile.value ?: return
        if (!consumeOxyOnAction(user)) return

        if (!user.canAttack && user.vipLevel < 1) {
            _activePopup.value = SatiricalPopup.RequiredPaywall(
                title = "🔒 CHƯA NẠP TIỀN BẤM NÚT!",
                message = "Bạn đang dùng bản Chơi Chùa! Vui lòng nạp gói 10.000đ để kích hoạt chức năng ĐÁNH SLIME!",
                priceVnd = 10000,
                actionName = "Kích Hoạt Nút Đánh (10k)",
                unlockType = "attack"
            )
        } else {
            // Deal damage
            if (_monsterHp.value > 1) {
                _monsterHp.value -= 5
                _systemNotice.value = "Bạn vừa chém Slime (-5 HP)! Slime cười khẩy: 'Dùng tay đánh dở lắm, nạp mua kiếm đi!'"
            } else {
                _monsterHp.value = _monsterMaxHp.value
                _systemNotice.value = "Slime đã hồi sinh! NPH quy định: Slime không bao giờ chết trừ khi nạp $99!"
            }
        }
    }

    fun onMoveClick() {
        incrementClickCount()
        val user = userProfile.value ?: return
        if (!consumeOxyOnAction(user)) return
        if (!user.canMove && user.vipLevel < 2) {
            _activePopup.value = SatiricalPopup.RequiredPaywall(
                title = "🔒 DLC DI CHUYỂN BỊ KHÓA!",
                message = "NPH quy định: Đứng yên một chỗ là miễn phí, di chuyển mất 20.000đ!",
                priceVnd = 20000,
                actionName = "Mua DLC Bức Tốc (20k)",
                unlockType = "move"
            )
        } else {
            _systemNotice.value = "Bạn bước sang phải 1 bước! Hệ thống khấu trừ 0đ vì bạn đã nạp DLC!"
        }
    }

    fun onAutoClick() {
        incrementClickCount()
        val user = userProfile.value ?: return
        if (!consumeOxyOnAction(user)) return
        if (!user.canAuto && user.vipLevel < 5) {
            _activePopup.value = SatiricalPopup.RequiredPaywall(
                title = "🔒 CHƯA ĐẠT VIP 5 AUTO!",
                message = "Chức năng Auto dành riêng cho giới thượng lưu! Nạp 100.000đ lên VIP 5 ngay!",
                priceVnd = 100000,
                actionName = "Thăng Cấp VIP 5 (100k)",
                unlockType = "auto"
            )
        } else {
            _systemNotice.value = "Đã bật Auto! Nhân vật tự động nạp tiền giùm bạn trong mơ!"
        }
    }

    fun onSoundClick() {
        incrementClickCount()
        val user = userProfile.value ?: return
        if (!consumeOxyOnAction(user)) return
        if (!user.canSound && user.vipLevel < 1) {
            _activePopup.value = SatiricalPopup.RequiredPaywall(
                title = "🔇 LOA GAME ĐANG BỊ KHÓA!",
                message = "Mở loa nghen âm thanh vàng bạc giòn giã! Giá dịch vụ: 15.000đ!",
                priceVnd = 15000,
                actionName = "Mở Âm Thanh (15k)",
                unlockType = "sound"
            )
        } else {
            _systemNotice.value = "Ting ting! Âm thanh tiền về ngân sách NPH thật êm tai!"
        }
    }

    fun onSettingsClick() {
        incrementClickCount()
        val user = userProfile.value ?: return
        if (!consumeOxyOnAction(user)) return
        if (!user.canSettings) {
            _activePopup.value = SatiricalPopup.RequiredPaywall(
                title = "⚙️ CÀI ĐẶT DÀNH CHO VIP",
                message = "Muốn chỉnh đồ họa hay thoát game? Hãy mua Gói Cài Đặt Bản Quyền 50.000đ!",
                priceVnd = 50000,
                actionName = "Mở Khóa Settings (50k)",
                unlockType = "settings"
            )
        } else {
            _systemNotice.value = "Menu Cài Đặt: Chỉ có 1 tùy chọn duy nhất -> 'NẠP THÊM TIỀN'!"
        }
    }

    fun processPurchase(
        packageName: String,
        amountVnd: Long,
        gemsGranted: Long,
        vipExpGranted: Int,
        paymentMethod: String,
        comment: String,
        unlockType: String? = null
    ) {
        viewModelScope.launch {
            repository.recordPurchase(
                packageName = packageName,
                amountVnd = amountVnd,
                gemsGranted = gemsGranted,
                vipExpGranted = vipExpGranted,
                paymentMethod = paymentMethod,
                comment = comment
            ) { user ->
                var u = user.copy(totalPopupsAccepted = user.totalPopupsAccepted + 1)
                when (unlockType) {
                    "attack" -> u = u.copy(canAttack = true)
                    "move" -> u = u.copy(canMove = true)
                    "auto" -> u = u.copy(canAuto = true)
                    "sound" -> u = u.copy(canSound = true)
                    "settings" -> u = u.copy(canSettings = true)
                    "oxy" -> u = u.copy(oxyLevel = 100, canBreatheForever = true)
                    "god" -> u = u.copy(isVip999God = true, canAttack = true, canMove = true, canAuto = true, canSound = true, canSettings = true, canBreatheForever = true)
                }
                u
            }
            _activePopup.value = null
            _systemNotice.value = "THÀNH CÔNG! Cảm ơn bạn đã cống nộp $amountVnd VNĐ cho NPH!"
            _moneyRainEvent.emit(Unit)
            PaySoundEffects.play(if (amountVnd >= 100000) MoneySoundType.JACKPOT_CELEBRATE else MoneySoundType.CASH_REGISTER)
        }
    }

    fun spinGachaWithGold() {
        incrementClickCount()
        val user = userProfile.value ?: return
        if (user.gold < 1000) {
            triggerBrokePopup(requiredGold = 1000)
            return
        }

        viewModelScope.launch {
            val updated = user.copy(gold = user.gold - 1000)
            repository.updateUser(updated)

            val outcomes = listOf(
                "Trúng: 1 Mảnh Đao Gỗ (Cần 999.999 mảnh để ghép)",
                "Trúng: Lời Cảm Ơn Tình Cảm Từ NPH",
                "Trúng: 0.0001 Kính Cường Lực Slime",
                "Trúng: Vé Mời Nạp Tiếp Lần Sau",
                "Trúng: Giảm Giá 0% Cho Gói Nạp Tiếp Theo"
            )
            val result = outcomes.random()
            _systemNotice.value = "VÒNG QUAY VÀNG: $result! (-1,000 Vàng)"
        }
    }

    fun spinGacha() {
        incrementClickCount()
        val user = userProfile.value ?: return
        if (user.gems < 100) {
            _activePopup.value = SatiricalPopup.CustomAlert(
                title = "💎 HẾT NGỌC VÒNG QUAY!",
                message = "Mỗi lượt quay tốn 100 Ngọc Kim Cương Nạp. Hãy cống nộp 20.000đ để lấy 200 Ngọc!",
                priceVnd = 20000
            )
            return
        }

        viewModelScope.launch {
            // Deduct 100 gems
            val updated = user.copy(gems = user.gems - 100)
            repository.updateUser(updated)

            val outcomes = listOf(
                "Trúng: 1 Mảnh Đao Gỗ (Cần 999.999 mảnh để ghép)",
                "Trúng: Lời Cảm Ơn Tình Cảm Từ NPH",
                "Trúng: 0.0001 Kính Cường Lực Slime",
                "Trúng: Vé Mời Nạp Tiếp Lần Sau",
                "Trúng: Giảm Giá 0% Cho Gói Nạp Tiếp Theo"
            )
            val result = outcomes.random()
            _systemNotice.value = "VÒNG QUAY: $result! Thật may mắn!"
        }
    }

    fun refillOxy() {
        processPurchase(
            packageName = "Gói Oxy Sinh Tồn 30s",
            amountVnd = 50000,
            gemsGranted = 500,
            vipExpGranted = 100,
            paymentMethod = "MoMo (Mô Phỏng)",
            comment = "Nạp tiền gia hạn sự sống trong game",
            unlockType = "oxy"
        )
    }

    fun setShowCertificate(show: Boolean) {
        _showCertificate.value = show
    }

    fun setPvpModeSelected(selected: Boolean) {
        _pvpModeSelected.value = selected
    }

    fun findNewPvpOpponent() {
        val opponents = listOf(
            Triple("👑 Cậu Ấm Nạp VIP15", 50000, 25000L),
            Triple("💸 Đại Gia Tỉ Phú Nạp 100Tr", 120000, 60000L),
            Triple("🐋 Whale-Lord_Server1", 80000, 40000L),
            Triple("🐤 Dân Cày Chay 0đ (Rớt Hạng)", 2000, 100L)
        )
        val selected = opponents.random()
        _pvpOpponentName.value = selected.first
        _pvpOpponentMaxHp.value = selected.second
        _pvpOpponentHp.value = selected.second
        _pvpOpponentPower.value = selected.third

        val newLogs = _pvpBattleLogs.value.toMutableList()
        newLogs.add("🔍 Đã ghép trận PvP mới! Đối thủ: ${selected.first} (Lực chiến: ${selected.third})")
        _pvpBattleLogs.value = newLogs.takeLast(20)
    }

    fun changePvpStance(stance: String) {
        _pvpStance.value = stance
        val newLogs = _pvpBattleLogs.value.toMutableList()
        newLogs.add("🔄 Bạn vừa chuyển sang Thế Trận: $stance")
        _pvpBattleLogs.value = newLogs.takeLast(20)
    }

    fun executePvpSkill(skillIndex: Int) {
        val user = userProfile.value ?: return
        if (!consumeOxyOnAction(user)) return

        val newLogs = _pvpBattleLogs.value.toMutableList()

        var baseDamage = _pvpPlayerPower.value
        if (_pvpStance.value == "ATTACK") baseDamage = (baseDamage * 1.5).toLong()

        // Combo check
        var currentCombo = _pvpComboStep.value
        if (skillIndex == currentCombo + 1) {
            currentCombo++
            if (currentCombo == 3) {
                baseDamage *= 3 // Huge combo damage!
                newLogs.add("💥 CHUỖI COMBO KÍCH HOẠT: THẦN LONG TRƯỜNG! (Gây x3 Sát thương)")
                currentCombo = 0
            } else {
                newLogs.add("⚡ Combo bước $currentCombo/3 thành công!")
            }
        } else {
            currentCombo = if (skillIndex == 1) 1 else 0
            newLogs.add("⚠️ Bạn bấm sai nhịp Combo Kỹ Năng! Chuỗi bị đứt!")
        }
        _pvpComboStep.value = currentCombo

        // Damage opponent
        val newOpponentHp = (_pvpOpponentHp.value - baseDamage).coerceAtLeast(0).toInt()
        _pvpOpponentHp.value = newOpponentHp
        newLogs.add("🗡️ Bạn đánh trúng ${_pvpOpponentName.value} gây -$baseDamage HP!")

        if (newOpponentHp <= 0) {
            newLogs.add("🏆 BẠN ĐÃ CHIẾN THẮNG TRẬN PVP! Nhận thưởng +5,000 Vàng Ảo!")
            addGold(5000)
            _systemNotice.value = "🎉 BẠN ĐÃ HẠ GỤC BÁ CHỦ PVP! Nhận +5.000 Vàng!"
        } else {
            // Opponent counter-attack if player not in GodMode
            if (_pvpGodModeTime.value <= 0) {
                val oppDmg = (_pvpOpponentPower.value * if (_pvpStance.value == "DEFENSE") 0.5 else 1.0).toInt()
                val newPlayerHp = (_pvpPlayerHp.value - oppDmg).coerceAtLeast(0)
                _pvpPlayerHp.value = newPlayerHp
                newLogs.add("💥 ${_pvpOpponentName.value} phản công lại gây -$oppDmg HP!")
                if (newPlayerHp <= 0) {
                    newLogs.add("☠️ BẠN BỊ ĐÁNH BẠI! Nạp tiền mua Gói Hồi Sinh / Tăng Sức Mạnh ngay!")
                    _activePopup.value = SatiricalPopup.CustomAlert(
                        title = "💀 BỊ BÁ CHỦ CỦA SERVER ĐÁNH BẠI!",
                        message = "Đối thủ quá giàu! Nạp ngay 20.000đ để có Bất Tử & Tăng +50.000 Lực Chiến!",
                        priceVnd = 20000
                    )
                }
            } else {
                newLogs.add("🛡️ Giáp Bất Tử của bạn đã cản toàn bộ sát thương từ đối thủ!")
            }
        }

        _pvpBattleLogs.value = newLogs.takeLast(20)
    }

    fun buyPvpPowerBoost() {
        if (spendGold(500)) {
            _pvpPlayerPower.value = _pvpPlayerPower.value + 50000L
            val newLogs = _pvpBattleLogs.value.toMutableList()
            newLogs.add("🚀 BẠN VỪA NẠP 500 VÀNG -> +50,000 LỰC CHIẾN TỨC THÌ! Sức mạnh hiện tại: ${_pvpPlayerPower.value}")
            _pvpBattleLogs.value = newLogs.takeLast(20)
            _systemNotice.value = "⚡ ĐÃ NẠP TIỀN P2W: Sức mạnh PvP tăng thêm +50,000 Lực Chiến!"
            PaySoundEffects.play(MoneySoundType.TING_TING)
        } else {
            _activePopup.value = SatiricalPopup.CustomAlert(
                title = "⚡ NẠP TIỀN TĂNG LỰC CHIẾN TỨC THÌ",
                message = "Nạp gói 10.000đ để nhận 100.000 Vàng & Tăng +50.000 Lực Chiến đè bẹp đối thủ!",
                priceVnd = 10000
            )
        }
    }

    fun buyPvpGodShield() {
        if (spendGold(1000)) {
            _pvpGodModeTime.value = 10
            val newLogs = _pvpBattleLogs.value.toMutableList()
            newLogs.add("🛡️ BẠN VỪA NẠP 1,000 VÀNG -> KÍCH HOẠT GIÁP BẤT TỬ 10 GIÂY!")
            _pvpBattleLogs.value = newLogs.takeLast(20)
            _systemNotice.value = "👑 ĐÃ BẬT GIÁP BẤT TỬ PVP 10 GIÂY!"
            PaySoundEffects.play(MoneySoundType.TING_TING)
        } else {
            _activePopup.value = SatiricalPopup.CustomAlert(
                title = "🛡️ NẠP TIỀN MUA GIÁP BẤT TỬ",
                message = "Không sợ bất kỳ đòn đánh nào! Nạp 20.000đ để bật Giáp Bất Tử 10 giây!",
                priceVnd = 20000
            )
        }
    }

    fun buyPvpInstantHeal() {
        if (spendGold(300)) {
            _pvpPlayerHp.value = _pvpPlayerMaxHp.value
            _pvpPlayerMana.value = 100
            val newLogs = _pvpBattleLogs.value.toMutableList()
            newLogs.add("🧪 BẠN VỪA NẠP 300 VÀNG -> HỒI 100% HP & MANA TỨC THÌ!")
            _pvpBattleLogs.value = newLogs.takeLast(20)
            _systemNotice.value = "🧪 ĐÃ HỒI ĐẦY MÁU & MANA TỨC THÌ!"
            PaySoundEffects.play(MoneySoundType.TING_TING)
        } else {
            triggerBrokePopup(300)
        }
    }

    fun buyPvpResetCd() {
        if (spendGold(200)) {
            _pvpSkill1Cd.value = 0
            _pvpSkill2Cd.value = 0
            _pvpSkill3Cd.value = 0
            val newLogs = _pvpBattleLogs.value.toMutableList()
            newLogs.add("⚡ BẠN VỪA NẠP 200 VÀNG -> XÓA TOÀN BỘ COOLDOWN KỸ NĂNG!")
            _pvpBattleLogs.value = newLogs.takeLast(20)
            PaySoundEffects.play(MoneySoundType.TING_TING)
        } else {
            triggerBrokePopup(200)
        }
    }

    fun buyPvpOneShot() {
        if (spendGold(50000)) {
            _pvpOpponentHp.value = 0
            val newLogs = _pvpBattleLogs.value.toMutableList()
            newLogs.add("💣 BẠN VỪA DÙNG CHIÊU MUA SERVER (50,000 VÀNG) -> ONE SHOT KILL ĐỐI THỦ!")
            newLogs.add("🏆 BẠN ĐÃ CHIẾN THẮNG TRẬN PVP!")
            _pvpBattleLogs.value = newLogs.takeLast(20)
            _systemNotice.value = "💣 CHIÊU MUA SERVER THÀNH CÔNG! ĐẠI GIA LÊN NGÔI!"
            PaySoundEffects.play(MoneySoundType.JACKPOT_CELEBRATE)
        } else {
            _activePopup.value = SatiricalPopup.CustomAlert(
                title = "💣 CHIÊU MUA SERVER (ONE SHOT KO)",
                message = "Nạp ngay gói 100.000đ nhận 500.000 Vàng để hạ gục đối thủ tức thì!",
                priceVnd = 100000
            )
        }
    }

    fun resetData() {
        viewModelScope.launch {
            repository.resetData()
            _systemNotice.value = "Đã dọn dẹp bộ nhớ. Hãy bắt đầu cống nộp lại từ đầu!"
        }
    }

    private fun startOxyTimer() {
        oxyJob?.cancel()
        oxyJob = viewModelScope.launch {
            while (true) {
                delay(8000)
                val user = userProfile.value ?: continue
                if (!user.canBreatheForever && user.oxyLevel > 0) {
                    val newOxy = (user.oxyLevel - 15).coerceAtLeast(0)
                    val updated = user.copy(oxyLevel = newOxy)
                    repository.updateUser(updated)

                    if (newOxy == 0 && _activePopup.value == null) {
                        _activePopup.value = SatiricalPopup.OxyWarning()
                        _systemNotice.value = "CẢNH BÁO CỰC KỲ NGUY CẤP: BẠN ĐANG HẾT OXY! NẠP TIỀN ĐỂ THỞ!"
                    }
                }
            }
        }
    }

    private fun startRandomPopupEngine() {
        randomPopupJob?.cancel()
        randomPopupJob = viewModelScope.launch {
            while (true) {
                delay(25000)
                if (_activePopup.value == null) {
                    triggerRandomFlashSale()
                }
            }
        }
    }
}
