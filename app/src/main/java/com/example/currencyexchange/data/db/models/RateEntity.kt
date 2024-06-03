package com.example.currencyexchange.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class RateEntity(
    @PrimaryKey
    val currency: String,
    val rate: BigDecimal,
)