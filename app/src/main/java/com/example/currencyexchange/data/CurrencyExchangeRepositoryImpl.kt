package com.example.currencyexchange.data

import android.util.Log
import com.example.currencyexchange.data.api.CurrencyExchangeApi
import com.example.currencyexchange.data.db.dao.AccountDao
import com.example.currencyexchange.data.db.dao.RatesDao
import com.example.currencyexchange.data.db.models.RateEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject

class CurrencyExchangeRepositoryImpl @Inject constructor(
    private val api: CurrencyExchangeApi,
    private val ratesDao: RatesDao,
    private val accountDao: AccountDao
) : CurrencyExchangeRepository {
    override suspend fun fetchRates() {
        val response = api.getRates()
        Log.d("CurrencyExchangeRepositoryImpl", response.toString())
        ratesDao.insertRates(response.rates.map { RateEntity(it.key, it.value) })
    }

    override fun getRatesForMyCurrencies(): Flow<List<RateEntity>> {
        return ratesDao.getRatesForMyCurrencies()
    }

    override suspend fun exchangeCurrency(
        fromAmount: BigDecimal,
        fromCurrency: String,
        toAmount: BigDecimal,
        toCurrency: String
    ) {
        val fromBalance = accountDao.getBalanceByCurrency(fromCurrency)
        val toBalance = accountDao.getBalanceByCurrency(toCurrency)

        val newFromBalance = fromBalance.amount.minus(fromAmount)
        val newToBalance = toBalance.amount.plus(toAmount)

        accountDao.updateTransactionalBalances(
            newFromBalance,
            fromCurrency,
            newToBalance,
            toCurrency
        )

    }
}