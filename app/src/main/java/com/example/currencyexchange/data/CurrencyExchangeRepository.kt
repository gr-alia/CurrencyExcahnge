package com.example.currencyexchange.data

import com.example.currencyexchange.data.db.models.RateEntity
import kotlinx.coroutines.flow.Flow

interface CurrencyExchangeRepository {

    suspend fun fetchRates()

    fun getRatesForMyCurrencies(): Flow<List<RateEntity>>
}