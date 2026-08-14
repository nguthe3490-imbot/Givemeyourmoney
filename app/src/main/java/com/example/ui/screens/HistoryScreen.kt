package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PurchaseEntity
import com.example.data.UserEntity
import com.example.ui.components.ReceiptZigZagEdge
import com.example.ui.theme.DarkGold
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent
import com.example.util.MoneySoundType
import com.example.util.PaySoundEffects
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Satirical hidden fees catalogue used for endless trolling receipt generator
private val SATIRICAL_FEES_CATALOGUE = listOf(
    Pair("Phí nhìn chằm chằm vào nút NẠP TIỀN", 5000L to "Liếc mắt nhìn nút nạp quá 3 giây"),
    Pair("Phí hiệu ứng mưa tiền rơi 60FPS", 15000L to "Thu tiền bản quyền hạt vàng bay lượn"),
    Pair("Phí âm thanh 'Ting Ting' phát qua loa", 10000L to "Bản quyền âm sắc kim loại va chạm"),
    Pair("Phí hít thở Oxy trong không gian Server", 30000L to "Hít không khí máy chủ mát lạnh"),
    Pair("Phí bảo dưỡng ví da Tổng Giám Đốc NPH", 500000L to "Ví bị chật vì nhận quá nhiều tiền của bạn"),
    Pair("Phí an ủi tâm lý sau khi quay Gacha xịt", 25000L to "Dịch vụ vỗ vai: 'Lần sau chắc chắn ra!'"),
    Pair("Phí bấm nhầm nút 'Hủy Nạp' (Phạt thái độ)", 20000L to "Dám có ý định từ chối nạp tiền"),
    Pair("Phí duy trì ảo tưởng sức mạnh PvP", 150000L to "Giúp bạn tin rằng mình đang outplay đối thủ"),
    Pair("Phí trả góp chiếc Porsche của Giám Đốc", 1000000L to "Góp 1 cái lốp xe cho sếp NPH"),
    Pair("Phí cà phê tăng ca của Lập Trình Viên", 20000L to "Dev thức đêm fix bug ép bạn nạp thêm"),
    Pair("Phí bảo hiểm cháy ví người chơi", 45000L to "Bồi thường 0 đồng khi bạn sạch túi"),
    Pair("Phí tài trợ du học con gái NPH", 2500000L to "Đầu tư cho thế hệ kế cận tiếp tục thu tiền"),
    Pair("Phí thuê Server Ping 999ms siêu mượt", 35000L to "Tạo cảm giác hồi hộp chờ kết quả"),
    Pair("Phí tạo danh hiệu Đại Gia ảo", 200000L to "Chữ vàng sáng chói làm mờ mắt thiên hạ"),
    Pair("Phí tồn tại hợp pháp trong Game", 80000L to "Chưa bị khóa acc là đã may mắn rồi"),
    Pair("Thuế giá trị ảo gia tăng VAT 100%", 990000L to "Thuế cống nộp tự nguyện"),
    Pair("Phí dịch vụ nghe tiếng máy đếm tiền Ka-Ching", 12000L to "Tạo cảm giác tài khoản đang nhiều tiền"),
    Pair("Phí chống cay cú khi bị Đại Gia khác đè bẹp", 60000L to "Lời khuyên: Hãy nạp nhiều hơn đối thủ"),
    Pair("Phí bản quyền câu chào: 'Chào Con Mồi VIP'", 18000L to "Được chào đón nồng hậu bởi hệ thống"),
    Pair("Phí bảo trì nút NẠP TIỀN 24/7", 75000L to "Đảm bảo nút nạp không bao giờ bị nghẽn"),
    Pair("Phí tư vấn tài chính ngược (Ép tiêu sạch tiền)", 110000L to "Chuyên gia khuyên nên cống nộp hết"),
    Pair("Phí tạo drama trên Bảng Xếp Hạng", 90000L to "Kích động các đại gia khác nạp đè lên bạn"),
    Pair("Phí dịch thuật hóa đơn sang tiếng Việt châm biếm", 0L to "Khuyến mãi 0đ cho người chơi thân thương"),
    Pair("Phí cống nộp không hồi kết đợt #99", 333000L to "Càng cuộn xuống càng thấy tốn tiền")
)

@Composable
fun HistoryScreen(
    user: UserEntity?,
    purchases: List<PurchaseEntity>,
    onOpenCertificateClick: () -> Unit,
    onResetDataClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currencyFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    val dateFormat = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale("vi", "VN"))
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var showResetDialog by remember { mutableStateOf(false) }
    var isAutoPrinting by remember { mutableStateOf(false) }
    var endlessItemCount by remember { mutableIntStateOf(30) }

    // Auto-scrolling / Auto-printing simulator
    LaunchedEffect(isAutoPrinting) {
        if (isAutoPrinting) {
            PaySoundEffects.play(MoneySoundType.MONEY_COUNTER)
            while (isAutoPrinting) {
                val current = listState.firstVisibleItemIndex
                listState.animateScrollToItem(
                    index = (current + 1).coerceAtMost(purchases.size + endlessItemCount + 5)
                )
                // If nearing end, dynamically append more endless satirical items!
                if (current >= purchases.size + endlessItemCount - 8) {
                    endlessItemCount += 20
                }
                delay(380)
            }
        }
    }

    // Troll Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = NeonRedAccent,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "⚠️ XÓA DẤU VẾT (GIẤU VỢ/MẸ)?",
                    fontWeight = FontWeight.Black,
                    color = NeonRedAccent,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    text = "Bạn chuẩn bị tiêu hủy toàn bộ bằng chứng cống nộp tiền cho NPH!\n\n" +
                            "• Danh hiệu Đại Gia sẽ bị tịch thu\n" +
                            "• Giấy chứng nhận VIP sẽ bị thu hồi\n" +
                            "• Nhưng số tiền bạn đã nạp thì NPH... KHÔNG HOÀN LẠI ĐÂU NHÉ! 💸\n\n" +
                            "Bạn có chắc muốn xóa lịch sử để giữ bình yên cho gia đình?",
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetDialog = false
                        onResetDataClick()
                        Toast.makeText(context, "🧹 Đã xóa sạch dấu vết cống nộp!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRedAccent)
                ) {
                    Text("XÓA NGAY (SỢ LẮM RỒI)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("GIỮ LẠI ĐỂ KHOE", color = GoldPrimary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color(0xFF1E142B),
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF110A1A))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        // Top Toolbar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = "Receipt",
                    tint = GoldPrimary,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "HÓA ĐƠN CỐNG NỘP VÔ TẬN",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = GoldPrimary
                    )
                    Text(
                        text = "Biên lai dài ngoằng trêu chọc con bạc",
                        fontSize = 10.sp,
                        color = Color.LightGray
                    )
                }
            }

            // Quick actions
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // Auto Print Toggle
                IconButton(
                    onClick = {
                        isAutoPrinting = !isAutoPrinting
                        if (isAutoPrinting) {
                            Toast.makeText(context, "🖨️ Đang in biên lai dài vô tận...", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (isAutoPrinting) GoldPrimary else Color(0xFF2A1B3D),
                            CircleShape
                        )
                        .border(1.dp, GoldPrimary, CircleShape)
                        .testTag("receipt_autoprint_button")
                ) {
                    Icon(
                        imageVector = if (isAutoPrinting) Icons.Default.Pause else Icons.Default.Print,
                        contentDescription = "Auto Print",
                        tint = if (isAutoPrinting) Color.Black else GoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Copy receipt text
                IconButton(
                    onClick = {
                        val totalVnd = user?.totalSpentVnd ?: 0L
                        val shareText = "🧾 BIÊN LAI CỐNG NỘP P2W CORP:\n" +
                                "Con mồi: ${user?.username ?: "Đại Gia Rỗng Ví"}\n" +
                                "Cấp bậc: VIP ${user?.vipLevel ?: 0}\n" +
                                "Tổng tiền đã cống nộp: ${currencyFormat.format(totalVnd)} VNĐ\n" +
                                "Đánh giá: ${getSatiricalRankTitle(totalVnd)}\n" +
                                "Xem thêm tại Game Pay2Win Simulator!"
                        clipboardManager.setText(AnnotatedString(shareText))
                        PaySoundEffects.play(MoneySoundType.TING_TING)
                        Toast.makeText(context, "📋 Đã sao chép biên lai để đi khoe chiến tích!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF2A1B3D), CircleShape)
                        .border(1.dp, DarkGold, CircleShape)
                        .testTag("receipt_copy_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = DarkGold,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Reset
                IconButton(
                    onClick = { showResetDialog = true },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF331122), CircleShape)
                        .border(1.dp, NeonRedAccent, CircleShape)
                        .testTag("receipt_reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = NeonRedAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Open Certificate Button Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onOpenCertificateClick,
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .testTag("history_certificate_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGold,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Certificate",
                    tint = GoldPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "XEM GIẤY CHỨNG NHẬN VIP",
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                )
            }

            // Fast scroll to bottom endless
            OutlinedButton(
                onClick = {
                    scope.launch {
                        endlessItemCount += 30
                        listState.animateScrollToItem(purchases.size + endlessItemCount)
                        PaySoundEffects.play(MoneySoundType.MONEY_COUNTER)
                    }
                },
                modifier = Modifier.height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardDoubleArrowDown,
                    contentDescription = "Scroll Down",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Cuộn Vô Tận", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Endless Thermal Paper Receipt Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(12.dp, RoundedCornerShape(4.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFDF5)) // Authentic thermal paper ivory
            ) {
                // Top Jagged Cut Edge
                ReceiptZigZagEdge(
                    isTop = true,
                    paperColor = Color(0xFFFFFDF5),
                    backgroundColor = Color(0xFF110A1A),
                    height = 6.dp
                )

                // Scrollable Thermal Paper Content
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    // Header Section
                    item {
                        ReceiptHeaderSection(
                            user = user,
                            dateFormat = dateFormat
                        )
                    }

                    // Grand Total Summary Box on Receipt
                    item {
                        ReceiptGrandTotalBox(
                            user = user,
                            currencyFormat = currencyFormat
                        )
                    }

                    // Real Purchases Section Header
                    item {
                        ReceiptDivider("DANH SÁCH CỐNG NỘP THỰC TẾ (${purchases.size} LẦN)")
                    }

                    if (purchases.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "⚠️ CHƯA CỐNG NỘP ĐỒNG NÀO!",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFB00020)
                                )
                                Text(
                                    text = "(Hãy nạp tiền ngay để tên bạn được khắc ghi trên bia đá NPH)",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = Color.DarkGray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        itemsIndexed(purchases) { index, item ->
                            ReceiptRealPurchaseRow(
                                index = index + 1,
                                item = item,
                                currencyFormat = currencyFormat,
                                dateFormat = dateFormat
                            )
                        }
                    }

                    // Endless Satirical Hidden Fees Section Header
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        ReceiptDivider("BẢNG KÊ PHỤ PHÍ ẨN BẤT TẬN (ENDLESS FEES)")
                        Text(
                            text = "💡 Hệ thống tự động truy thu 1001 loại phí vô lý khi bạn tiếp tục cuộn xuống...",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.5.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    // Endless Procedural Satirical Items
                    items(endlessItemCount) { index ->
                        val fee = SATIRICAL_FEES_CATALOGUE[index % SATIRICAL_FEES_CATALOGUE.size]
                        val dynamicPrice = fee.second.first + ((index / SATIRICAL_FEES_CATALOGUE.size) * 10000L)
                        ReceiptSatiricalFeeRow(
                            index = index + 1,
                            name = fee.first,
                            reason = fee.second.second,
                            amountVnd = dynamicPrice,
                            currencyFormat = currencyFormat
                        )
                    }

                    // Dynamic Load More Trigger & Tease Footer
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⏬ CUỘN TIẾP ĐỂ PHÁT HIỆN THÊM CÁC KHOẢN PHÍ MỚI...",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFFC75D00)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedButton(
                                onClick = {
                                    endlessItemCount += 25
                                    PaySoundEffects.play(MoneySoundType.TING_TING)
                                },
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "KÉO DÀI THÊM 25 KHOẢN PHÍ TỰ NGHĨ RA",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF110A1A)
                                )
                            }
                        }
                    }

                    // Barcode & Satirical Stamp Footer
                    item {
                        ReceiptFooterSection()
                    }
                }

                // Bottom Jagged Cut Edge
                ReceiptZigZagEdge(
                    isTop = false,
                    paperColor = Color(0xFFFFFDF5),
                    backgroundColor = Color(0xFF110A1A),
                    height = 6.dp
                )
            }

            // Floating Scroll to Top button if scrolled down
            if (listState.firstVisibleItemIndex > 3) {
                IconButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(42.dp)
                        .background(Color(0xFF1E142B), CircleShape)
                        .border(1.5.dp, GoldPrimary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardDoubleArrowUp,
                        contentDescription = "Scroll to top",
                        tint = GoldPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

/**
 * Top header of the thermal receipt
 */
@Composable
private fun ReceiptHeaderSection(
    user: UserEntity?,
    dateFormat: SimpleDateFormat
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "★ TẬP ĐOÀN HÚT MÁU QUỐC TẾ P2W ★",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.5.sp,
            color = Color(0xFF111111),
            textAlign = TextAlign.Center
        )
        Text(
            text = "CHI NHÁNH: TẬN THU TỪNG ĐỒNG VÍ TIỀN",
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = Color(0xFF444444),
            textAlign = TextAlign.Center
        )
        Text(
            text = "MÃ SỐ THUẾ: 000-LAM-TIEN-THAT-DE",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = Color(0xFF666666)
        )
        Text(
            text = "--------------------------------------------------",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color(0xFF888888)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "HĐ: #P2W-VIP-${user?.id ?: 1}099",
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = Color.Black
            )
            Text(
                text = dateFormat.format(Date()),
                fontFamily = FontFamily.Monospace,
                fontSize = 9.5.sp,
                color = Color(0xFF555555)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "CON MỒI: ${user?.username ?: "Đại Gia Ẩn Danh"}",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                color = Color.Black
            )
            Text(
                text = "CẤP ĐỘ: VIP ${user?.vipLevel ?: 0}",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                color = Color(0xFFC75D00)
            )
        }
    }
}

/**
 * Total Money Spent highlighted block on the receipt
 */
@Composable
private fun ReceiptGrandTotalBox(
    user: UserEntity?,
    currencyFormat: NumberFormat
) {
    val totalVnd = user?.totalSpentVnd ?: 0L
    val rankTitle = getSatiricalRankTitle(totalVnd)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFF4EED8), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(4.dp))
            .padding(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "TỔNG SỐ TIỀN ĐÃ CỐNG NỘP THỰC TẾ",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                color = Color(0xFF555555)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${currencyFormat.format(totalVnd)} VNĐ",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = Color(0xFFB00020)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "🏆 ĐÁNH GIÁ TỪ NPH:",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = Color(0xFF222222)
            )
            Text(
                text = "\"$rankTitle\"",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 11.5.sp,
                color = Color(0xFF8B5A00),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Row displaying an authentic real in-app purchase
 */
@Composable
private fun ReceiptRealPurchaseRow(
    index: Int,
    item: PurchaseEntity,
    currencyFormat: NumberFormat,
    dateFormat: SimpleDateFormat
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index. ${item.packageName}",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = Color(0xFF111111),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${currencyFormat.format(item.amountVnd)} đ",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                fontSize = 11.5.sp,
                color = Color(0xFFB00020)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "  PT: ${item.paymentMethod}",
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                color = Color(0xFF555555)
            )
            Text(
                text = dateFormat.format(Date(item.timestamp)),
                fontFamily = FontFamily.Monospace,
                fontSize = 8.5.sp,
                color = Color(0xFF777777)
            )
        }
        Text(
            text = "  ↳ Lời bình: \"${item.satiricalComment}\"",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF6B4423)
        )
        Text(
            text = " - - - - - - - - - - - - - - - - - - - - - - - - - -",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = Color(0xFFCCCCCC)
        )
    }
}

/**
 * Row displaying a satirical hidden fee line item
 */
@Composable
private fun ReceiptSatiricalFeeRow(
    index: Int,
    name: String,
    reason: String,
    amountVnd: Long,
    currencyFormat: NumberFormat
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#F$index. $name",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "+${currencyFormat.format(amountVnd)} đ",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 10.5.sp,
                color = Color(0xFF777777)
            )
        }
        Text(
            text = "  ↳ Lí do: $reason",
            fontFamily = FontFamily.Monospace,
            fontSize = 8.5.sp,
            color = Color(0xFF888888)
        )
    }
}

/**
 * Barcode, stamp, and footer
 */
@Composable
private fun ReceiptFooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "==================================================",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color(0xFF888888)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Red Rubber Stamp of Satire
        Box(
            modifier = Modifier
                .rotate(-7f)
                .border(2.dp, Color(0xFFCC0000), RoundedCornerShape(6.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "★ ĐÃ CỐNG NỘP THÀNH CÔNG ★",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = Color(0xFFCC0000)
                )
                Text(
                    text = "MIỄN TRẢ LẠI TIỀN DƯỚI MỌI HÌNH THỨC",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp,
                    color = Color(0xFFCC0000)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Thermal Barcode graphic
        Text(
            text = "||||| || |||||| | |||| |||||||| || |||||| ||| ||||",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = Color.Black,
            letterSpacing = 2.sp
        )
        Text(
            text = "*999-HUT-MAU-P2W-FOREVER*",
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            color = Color(0xFF555555)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "CẢM ƠN QUÝ KHÁCH ĐÃ NUÔI SỐNG CẢ CÔNG TY CHÚNG TÔI!\n" +
                    "HẸN GẶP LẠI Ở LẦN NẠP TIẾP THEO KHI BẠN THUA PVP.",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 9.5.sp,
            color = Color(0xFF444444),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ReceiptDivider(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = "--------------------------------------------------",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color(0xFF888888)
        )
        Text(
            text = "▶ $title",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            color = Color(0xFF111111)
        )
        Text(
            text = "--------------------------------------------------",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            color = Color(0xFF888888)
        )
    }
}

/**
 * Returns satirical rank title based on total spent VND
 */
private fun getSatiricalRankTitle(totalVnd: Long): String {
    return when {
        totalVnd == 0L -> "Cá Cơm Ăn Chực (Chưa cống nộp đồng nào cho NPH)"
        totalVnd < 100000L -> "Con Mồi Khởi Động (Ví bắt đầu có dấu hiệu rung lắc)"
        totalVnd < 500000L -> "Nhà Tài Trợ Tiềm Năng (Bắt đầu nuôi sống đội Dev)"
        totalVnd < 2000000L -> "Đại Gia Hào Sảng (Trụ cột tài chính vững chắc của NPH)"
        totalVnd < 10000000L -> "Cá Voi VIP Kim Cương (Bảo trợ du học cho con Giám Đốc)"
        else -> "Chúa Tể Cống Nộp (Mua đứt luôn cả Server và trụ sở NPH!)"
    }
}
