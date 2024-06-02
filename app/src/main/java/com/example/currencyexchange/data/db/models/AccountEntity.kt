package com.example.currencyexchange.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountEntity(
    @PrimaryKey
    val id: Long,
    val name: String
)