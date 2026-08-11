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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEntity
import com.example.ui.theme.DarkGold
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonRedAccent
import java.text.NumberFormat
import java.util.Locale

data class LeaderboardPlayer(
    val name: String,
    val totalSpentVnd: Long,
    val vipLevel: Int,
    val isCurrentUser: Boolean = false
)

@Composable
fun LeaderboardScreen(
    user: UserEntity?,
    modifier: Modifier = Modifier
) {
    val currencyFormat = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    val currentUserSpent = user?.totalSpentVnd ?: 0
    val currentUserName = user?.username ?: "Nô Lệ Pay2Win (Bạn)"
    val currentUserVip = user?.vipLevel ?: 0

    val fakePlayers = listOf(
        LeaderboardPlayer("ĐạiGiaNạp5Tỷ", 5000000000L, 999),
        LeaderboardPlayer("ThánhCầmSổĐỏ", 2800000000L, 999),
        LeaderboardPlayer("NạpMớiSốngĐược", 1200000000L, 99),
        LeaderboardPlayer("ThuaCũngNạp", 500000000L, 99),
        LeaderboardPlayer("XómLaoĐộngPlay2Win", 100000000L, 10),
        LeaderboardPlayer("DânChơiGiaĐình", 20000000L, 5)
    )

    val currentUserPlayer = LeaderboardPlayer(
        name = "$currentUserName (Bạn)",
        totalSpentVnd = currentUserSpent,
        vipLevel = currentUserVip,
        isCurrentUser = true
    )

    val allPlayers = (fakePlayers + currentUserPlayer).sortedByDescending { it.totalSpentVnd }
    val userRank = allPlayers.indexOfFirst { it.isCurrentUser } + 1

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF130D1D))
            .padding(12.dp)
    ) {
        // Leaderboard Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Leaderboard",
                tint = GoldPrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "BXH ĐẠI GIA CỐNG NỘP",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = GoldPrimary
                )
                Text(
                    text = "Xếp hạng dựa trên TỔNG TIỀN VNĐ đã đưa cho NPH",
                    fontSize = 11.sp,
                    color = Color.LightGray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Your Rank Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GoldPrimary, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E1C42)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "VỊ TRÍ CỦA BẠN", fontSize = 10.sp, color = Color.Gray)
                    Text(
                        text = "Hạng #$userRank toàn vũ trụ",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = GoldPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Đã Cống Nộp:", fontSize = 10.sp, color = Color.Gray)
                    Text(
                        text = "${currencyFormat.format(currentUserSpent)}đ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(allPlayers) { index, player ->
                val rank = index + 1
                LeaderboardRowItem(
                    rank = rank,
                    player = player,
                    currencyFormat = currencyFormat
                )
            }
        }
    }
}

@Composable
private fun LeaderboardRowItem(
    rank: Int,
    player: LeaderboardPlayer,
    currencyFormat: NumberFormat
) {
    val rankColor = when (rank) {
        1 -> GoldPrimary
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                if (player.isCurrentUser) 2.dp else 0.5.dp,
                if (player.isCurrentUser) GoldPrimary else Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .testTag("leaderboard_row_$rank"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (player.isCurrentUser) Color(0xFF381F54) else Color(0xFF1E162B)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Rank Badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(rankColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#$rank",
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = player.name,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (player.isCurrentUser) GoldPrimary else Color.White,
                            fontSize = 13.sp
                        )
                        if (player.isCurrentUser) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "(BẠN)",
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                color = NeonRedAccent
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "VIP",
                            tint = GoldPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "VIP ${player.vipLevel}",
                            fontSize = 11.sp,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${currencyFormat.format(player.totalSpentVnd)}đ",
                    fontWeight = FontWeight.Black,
                    color = GoldPrimary,
                    fontSize = 13.sp
                )
                Text(
                    text = "Đã nạp",
                    fontSize = 9.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
