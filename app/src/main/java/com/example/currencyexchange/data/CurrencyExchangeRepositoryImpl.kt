package com.example.currencyexchange.data

import android.util.Log
import com.example.currencyexchange.data.api.CurrencyExchangeApi
import com.example.currencyexchange.data.db.dao.RatesDao
import com.example.currencyexchange.data.db.models.RateEntity
import javax.inject.Inject

class CurrencyExchangeRepositoryImpl @Inject constructor(
    private val api: CurrencyExchangeApi,
    private val ratesDao: RatesDao
) : CurrencyExchangeRepository {
    override suspend fun fetchRates() {
        val response = api.getRates()
        Log.d("CurrencyExchangeRepositoryImpl", response.toString())
        ratesDao.insertRates(response.rates.map { RateEntity(it.key, it.value) })
    }
}