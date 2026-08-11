package com.example.ui.components

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.UserEntity
import com.example.ui.theme.DarkGold
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.VipPurple
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CertificateDialog(
    user: UserEntity?,
    onDismiss: () -> Unit
) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, Brush.horizontalGradient(listOf(GoldPrimary, DarkGold, GoldPrimary)), RoundedCornerShape(16.dp))
                .testTag("certificate_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF3E0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.DarkGray)
                    }
                }

                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = "Verified",
                    tint = DarkGold,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "GIẤY CHỨNG NHẬN CỐNG NỘP",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = Color(0xFF4A2E00),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "BẢN QUYỀN NPH PAY2WIN VŨ TRỤ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B5A2B),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Chứng nhận Người chơi:",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = user?.username ?: "Nô Lệ Pay2Win",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2B1B00)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Đã cống nộp tổng cộng:",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "${currencyFormat.format(user?.totalSpentVnd ?: 0)} VNĐ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFB8860B)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = "Star", tint = DarkGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ĐẠT CẤP ĐỘ: VIP ${user?.vipLevel ?: 0}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = VipPurple
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Lời Tuyên Hứa: \"Tôi cam kết sẽ nạp tiền liên tục, không phàn nàn về lỗi game, và coi NPH là lẽ sống!\"",
                    fontSize = 11.sp,
                    color = Color(0xFF555555),
                    textAlign = TextAlign.Center,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGold, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "ĐÃ KÝ TÊN VÀ CHẤP NHẬN", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
