package com.example.currencyexchange.data.api

import com.example.currencyexchange.data.api.models.AccountDTO
import com.example.currencyexchange.data.api.models.BalanceDTO
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.util.Currency
import javax.inject.Inject

class AccountRemoteDataSourceImpl @Inject constructor() : AccountRemoteDataSource {

    override suspend fun getAccount(): AccountDTO {
        // pretends to be a request to the server to get data
        delay(2000)
        return AccountDTO(
            id = 1111,
            name = "My multi-currency account",
            balances = listOf(
                BalanceDTO(BigDecimal(1000), Currency.getInstance("EUR")),
                BalanceDTO(BigDecimal(0), Currency.getInstance("USD")),
                BalanceDTO(BigDecimal(0), Currency.getInstance("GBP")),
            )
        )
    }
}