package com.example.data

import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val userDao: UserDao,
    private val purchaseDao: PurchaseDao
) {
    val userProfile: Flow<UserEntity?> = userDao.getUserProfile()
    val allPurchases: Flow<List<PurchaseEntity>> = purchaseDao.getAllPurchases()

    suspend fun getOrCreateUser(): UserEntity {
        var user = userDao.getUserProfileSync()
        if (user == null) {
            user = UserEntity()
            userDao.insertOrUpdateUser(user)
        }
        return user
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.insertOrUpdateUser(user)
    }

    suspend fun recordPurchase(
        packageName: String,
        amountVnd: Long,
        gemsGranted: Long,
        vipExpGranted: Int,
        paymentMethod: String,
        comment: String,
        unlockAction: (UserEntity) -> UserEntity
    ) {
        val currentUser = getOrCreateUser()
        val newSpent = currentUser.totalSpentVnd + amountVnd
        val newGems = currentUser.gems + gemsGranted
        val newVipExp = currentUser.vipExp + vipExpGranted
        val newVipLevel = calculateVipLevel(newVipExp)

        var updatedUser = currentUser.copy(
            totalSpentVnd = newSpent,
            gems = newGems,
            vipExp = newVipExp,
            vipLevel = newVipLevel
        )
        updatedUser = unlockAction(updatedUser)

        userDao.insertOrUpdateUser(updatedUser)

        val purchase = PurchaseEntity(
            packageName = packageName,
            amountVnd = amountVnd,
            gemsGranted = gemsGranted,
            vipExpGranted = vipExpGranted,
            paymentMethod = paymentMethod,
            satiricalComment = comment
        )
        purchaseDao.insertPurchase(purchase)
    }

    suspend fun resetData() {
        purchaseDao.clearHistory()
        userDao.insertOrUpdateUser(UserEntity())
    }

    private fun calculateVipLevel(exp: Int): Int {
        return when {
            exp >= 10000 -> 999
            exp >= 5000 -> 99
            exp >= 2000 -> 10
            exp >= 1000 -> 5
            exp >= 500 -> 3
            exp >= 200 -> 2
            exp >= 50 -> 1
            else -> 0
        }
    }
}
