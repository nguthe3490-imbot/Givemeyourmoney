package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "Nô Lệ Pay2Win #9999",
    val vipLevel: Int = 0,
    val vipExp: Int = 0,
    val gold: Long = 100,
    val gems: Long = 0,
    val totalSpentVnd: Long = 0,
    val totalPopupsClosed: Int = 0,
    val totalPopupsAccepted: Int = 0,
    val oxyLevel: Int = 100, // 0 to 100
    // Unlocked features (satirical paywalls)
    val canAttack: Boolean = false,
    val canMove: Boolean = false,
    val canAuto: Boolean = false,
    val canSound: Boolean = false,
    val canSettings: Boolean = false,
    val canBreatheForever: Boolean = false,
    val isVip999God: Boolean = false
)
