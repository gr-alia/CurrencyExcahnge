package com.example.currencyexchange.data.mappers

import com.example.currencyexchange.data.api.models.AccountDTO
import com.example.currencyexchange.data.api.models.BalanceDTO
import com.example.currencyexchange.data.db.models.AccountEntity
import com.example.currencyexchange.data.db.models.BalanceEntity

fun AccountDTO.toEntity(): AccountEntity = AccountEntity(
    id,
    name
)

fun BalanceDTO.toEntity(accountId: Long): BalanceEntity = BalanceEntity(
    id = currency.currencyCode,
    accountId = accountId,
    amount = amount
)