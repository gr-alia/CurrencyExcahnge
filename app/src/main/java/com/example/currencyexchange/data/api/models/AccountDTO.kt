package com.example.currencyexchange.data.api.models

class AccountDTO(
    val id: Long,
    val name: String,
    val balances: List<BalanceDTO>
)