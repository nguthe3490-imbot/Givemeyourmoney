package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchase_history")
data class PurchaseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val amountVnd: Long,
    val gemsGranted: Long,
    val vipExpGranted: Int,
    val paymentMethod: String, // "MoMo (Giả Lập)", "ZaloPay", "Sổ Đỏ", "Ví Thần Kỳ"
    val timestamp: Long = System.currentTimeMillis(),
    val satiricalComment: String
)
