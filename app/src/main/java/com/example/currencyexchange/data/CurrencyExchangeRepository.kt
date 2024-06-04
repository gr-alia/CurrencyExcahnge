package com.example.currencyexchange.data

import com.example.currencyexchange.data.db.models.RateEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface CurrencyExchangeRepository {

    suspend fun fetchRates()

    fun getRatesForMyCurrencies(): Flow<List<RateEntity>>

    suspend fun exchangeCurrency(
        fromAmount: BigDecimal,
        fromCurrency: String,
        toAmount: BigDecimal,
        toCurrency: String
    )
}