package com.example.ui.components

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.DarkGold
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PayPopupModal(
    title: String,
    message: String,
    priceVnd: Long,
    actionName: String,
    onDismiss: () -> Unit,
    onConfirmPay: (paymentMethod: String) -> Unit
) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    var selectedMethod by remember { mutableStateOf("MoMo Mô Phỏng") }

    val paymentMethods = listOf(
        Triple("MoMo Mô Phỏng", "⚡ Quẹt Nhanh Bay Ví", Icons.Default.QrCode),
        Triple("ZaloPay Hút Máu", "🩸 Trừ Tiền Tự Động", Icons.Default.CreditCard),
        Triple("Cầm Sổ Đỏ", "🏠 Đổi Nhà Lấy VIP", Icons.Default.Home),
        Triple("Ví Thần Kỳ", "🔮 Hết Tiền Vẫn Cho Nạp", Icons.Default.MonetizationOn)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Brush.linearGradient(listOf(GoldPrimary, NeonRedAccent)), RoundedCornerShape(20.dp))
                .testTag("pay_popup_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E142B))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Flash Icon & Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(NeonRedAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = "Flash",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "THÔNG BÁO TỪ NPH",
                            color = GoldPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("dismiss_popup_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.LightGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title & Message
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    color = Color(0xFFE0D6F0),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Price Tag
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFF3B1E54), Color(0xFF522258))),
                            RoundedCornerShape(12.dp)
                        )
                        .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "GIÁ CỐNG NỘP DỰ KIẾN",
                            fontSize = 11.sp,
                            color = Color.LightGray
                        )
                        Text(
                            text = "${currencyFormat.format(priceVnd)} VNĐ",
                            fontWeight = FontWeight.Black,
                            color = GoldPrimary,
                            fontSize = 22.sp
                        )
                        Text(
                            text = "Tặng ngay 100 EXP VIP + Lời chúc từ NPH",
                            fontSize = 10.sp,
                            color = Color(0xFFA0E0A0)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CHỌN HÌNH THỨC CỐNG NỘP:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Payment Method Selector
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    paymentMethods.forEach { (name, desc, icon) ->
                        val isSelected = selectedMethod == name
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) Color(0xFF4A1E6D) else Color(0xFF140D1E),
                                    RoundedCornerShape(10.dp)
                                )
                                .border(
                                    if (isSelected) 1.5.dp else 0.5.dp,
                                    if (isSelected) GoldPrimary else Color.Gray,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedMethod = name }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = name,
                                tint = if (isSelected) GoldPrimary else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = name,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color.LightGray,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = desc,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Button(
                    onClick = { onConfirmPay(selectedMethod) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_pay_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "💳 THANH TOÁN NGAY ($actionName)",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Bỏ qua cơ hội thành Đại Gia (Tắt Popup)",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
