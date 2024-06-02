package com.example.currencyexchange.data.db.models

import androidx.room.Embedded
import androidx.room.Relation

data class AccountWithBalances(
    @Embedded
    val account: AccountEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId"
    )
    val balances: List<BalanceEntity>
)