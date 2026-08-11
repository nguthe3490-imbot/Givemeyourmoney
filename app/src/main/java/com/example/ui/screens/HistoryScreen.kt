package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PurchaseEntity
import com.example.data.UserEntity
import com.example.ui.theme.DarkGold
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    user: UserEntity?,
    purchases: List<PurchaseEntity>,
    onOpenCertificateClick: () -> Unit,
    onResetDataClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    val dateFormat = SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale("vi", "VN"))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF130D1E))
            .padding(12.dp)
    ) {
        // Title & Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = "History",
                    tint = GoldPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "NHẬT KÝ CỐNG NỘP",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = GoldPrimary
                    )
                    Text(
                        text = "Bằng chứng bạn đã đưa tiền cho NPH",
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
            }

            IconButtonWithLabel(
                icon = Icons.Default.Delete,
                label = "Xóa Dữ Liệu",
                onClick = onResetDataClick
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Open Certificate Button
        Button(
            onClick = onOpenCertificateClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("history_certificate_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGold,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Certificate",
                    tint = GoldPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "XEM GIẤY CHỨNG NHẬN VIP CỐNG NỘP",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (purchases.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "💸 BẠN CHƯA CỐNG NỘP DỒNG NÀO!",
                        fontWeight = FontWeight.Black,
                        color = GoldPrimary,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Hãy vào Cửa Hàng nạp tiền ngay để trở thành VIP chân chính!",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(purchases) { item ->
                    PurchaseHistoryCard(
                        item = item,
                        currencyFormat = currencyFormat,
                        dateFormat = dateFormat
                    )
                }
            }
        }
    }
}

@Composable
private fun PurchaseHistoryCard(
    item: PurchaseEntity,
    currencyFormat: NumberFormat,
    dateFormat: SimpleDateFormat
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F172C)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.packageName,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = "${currencyFormat.format(item.amountVnd)} VNĐ",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = GoldPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Hình thức: ${item.paymentMethod}",
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
                Text(
                    text = dateFormat.format(Date(item.timestamp)),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "💬 ${item.satiricalComment}",
                fontSize = 11.sp,
                color = Color(0xFFCDB4DB),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

@Composable
private fun IconButtonWithLabel(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonRedAccent),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.testTag("reset_data_button")
    ) {
        Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}
