package com.example.currencyexchange.data.api.models

import java.math.BigDecimal

data class RatesResponse(
    val base: String,
    val date: String,
    val rates: Map<String,BigDecimal>,
)
