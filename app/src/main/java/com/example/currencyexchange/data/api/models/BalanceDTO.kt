package com.example.currencyexchange.data.api.models

import java.math.BigDecimal
import java.util.Currency

class BalanceDTO(
    val amount: BigDecimal,
    val currency: Currency
)