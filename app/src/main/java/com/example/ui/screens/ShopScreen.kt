package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGold
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent
import com.example.ui.theme.VipPurple
import java.text.NumberFormat
import java.util.Locale

data class ShopPackage(
    val id: String,
    val name: String,
    val priceVnd: Long,
    val gems: Long,
    val vipExp: Int,
    val badgeText: String,
    val description: String,
    val unlockType: String?,
    val icon: ImageVector,
    val isPopular: Boolean = false,
    val category: String = "feature" // "starter", "feature", "vip"
)

@Composable
fun ShopScreen(
    onBuyPackage: (pkg: ShopPackage) -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    var selectedCategory by remember { mutableStateOf("all") }

    val packages = listOf(
        ShopPackage(
            id = "pkg_10k",
            name = "Gói Tập Sự 'Học Cách Nạp'",
            priceVnd = 10000,
            gems = 100,
            vipExp = 50,
            badgeText = "HỜI 999%",
            description = "🔑 Mở khóa nút Tấn Công Chém Slime + Tặng 100 Ngọc Nạp",
            unlockType = "attack",
            icon = Icons.Default.CardGiftcard,
            category = "starter"
        ),
        ShopPackage(
            id = "pkg_20k",
            name = "Gói Dân Chơi Xóm 'DLC Bứt Tốc'",
            priceVnd = 20000,
            gems = 250,
            vipExp = 100,
            badgeText = "BEST SELLER",
            description = "🏃 Mở khóa nút Di Chuyển Qua Phải + Tặng 250 Ngọc Nạp",
            unlockType = "move",
            icon = Icons.Default.LocalAtm,
            isPopular = true,
            category = "feature"
        ),
        ShopPackage(
            id = "pkg_50k",
            name = "Gói Bảo Hiểm Oxy Vĩnh Cửu",
            priceVnd = 50000,
            gems = 700,
            vipExp = 250,
            badgeText = "SINH TỒN",
            description = "🫁 Cho phép nhân vật Hít Thở Vĩnh Cửu không bị chết vì hết Oxy!",
            unlockType = "oxy",
            icon = Icons.Default.Shield,
            isPopular = true,
            category = "feature"
        ),
        ShopPackage(
            id = "pkg_100k",
            name = "Gói VIP 5 Treo Máy Auto",
            priceVnd = 100000,
            gems = 1500,
            vipExp = 600,
            badgeText = "VIP 5 ULTRA",
            description = "🤖 Tự động chém Slime & Tự động nạp tiền giùm bạn!",
            unlockType = "auto",
            icon = Icons.Default.WorkspacePremium,
            category = "vip"
        ),
        ShopPackage(
            id = "pkg_500k",
            name = "Gói Đại Gia Vũ Trụ",
            priceVnd = 500000,
            gems = 9000,
            vipExp = 3000,
            badgeText = "THIẾU GIA",
            description = "👑 Lên thẳng VIP 10 + Bật Kim Tuyến Lấp Lánh toàn màn hình!",
            unlockType = "god",
            icon = Icons.Default.Star,
            category = "vip"
        ),
        ShopPackage(
            id = "pkg_10m",
            name = "Gói Cầm Cố Sổ Đỏ 'Chúa Tể P2W'",
            priceVnd = 10000000,
            gems = 250000,
            vipExp = 50000,
            badgeText = "SIÊU TRÙM",
            description = "🏛️ NPH gửi thư tay cảm ơn + Lên thẳng VIP 999 Chúa Tể Server!",
            unlockType = "god",
            icon = Icons.Default.LocalFireDepartment,
            isPopular = true,
            category = "vip"
        )
    )

    val filteredPackages = remember(selectedCategory) {
        if (selectedCategory == "all") packages else packages.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D0814))
            .padding(12.dp)
    ) {
        // Shop Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Brush.linearGradient(listOf(GoldPrimary, DarkGold)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = "Shop",
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "CỬA HÀNG CỐNG NỘP NPH",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = GoldPrimary
                )
                Text(
                    text = "Nạp càng nhiều - NPH càng trân trọng bạn!",
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                CategoryChip(
                    title = "🔥 Tất Cả (${packages.size})",
                    isSelected = selectedCategory == "all",
                    onClick = { selectedCategory = "all" }
                )
            }
            item {
                CategoryChip(
                    title = "🎁 Tân Thủ",
                    isSelected = selectedCategory == "starter",
                    onClick = { selectedCategory = "starter" }
                )
            }
            item {
                CategoryChip(
                    title = "🔑 Mở Tính Năng",
                    isSelected = selectedCategory == "feature",
                    onClick = { selectedCategory = "feature" }
                )
            }
            item {
                CategoryChip(
                    title = "👑 Gói VIP / Đỉnh Cao",
                    isSelected = selectedCategory == "vip",
                    onClick = { selectedCategory = "vip" }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredPackages) { pkg ->
                ShopItemCard(
                    pkg = pkg,
                    currencyFormat = currencyFormat,
                    onBuyClick = { onBuyPackage(pkg) }
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) GoldPrimary else Color(0xFF231833),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isSelected) GoldPrimary else Color(0xFF3B2A52),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
            color = if (isSelected) Color.Black else Color.White
        )
    }
}

@Composable
private fun ShopItemCard(
    pkg: ShopPackage,
    currencyFormat: NumberFormat,
    onBuyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                if (pkg.isPopular) 2.dp else 1.dp,
                if (pkg.isPopular) GoldPrimary else Color(0xFF3C2C50),
                RoundedCornerShape(16.dp)
            )
            .testTag("shop_item_${pkg.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C132B))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                Brush.radialGradient(listOf(GoldPrimary, DarkGold)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = pkg.icon,
                            contentDescription = pkg.name,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = pkg.name,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Gems",
                                tint = DiamondCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "+${currencyFormat.format(pkg.gems)} Ngọc Nạp | +${pkg.vipExp} VIP EXP",
                                color = DiamondCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .background(
                            if (pkg.isPopular) GoldPrimary else NeonRedAccent,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = pkg.badgeText,
                        color = if (pkg.isPopular) Color.Black else Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = pkg.description,
                color = Color(0xFFD4C9E6),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onBuyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("buy_button_${pkg.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "💳 NẠP NGAY ${currencyFormat.format(pkg.priceVnd)} VNĐ",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }
        }
    }
}

