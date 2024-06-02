package com.example.currencyexchange.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class BalanceEntity(
    @PrimaryKey
    val id: String,
    val accountId: Long,
    val amount: BigDecimal,
)