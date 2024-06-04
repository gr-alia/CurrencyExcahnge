package com.example.currencyexchange.domain

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ConvertCurrencyUseCase @Inject constructor() {

    fun convertCurrency(
        fromAmount: BigDecimal,
        fromCurrencyRate: BigDecimal,
        toCurrencyRate: BigDecimal
    ): BigDecimal {
        val amountInBaseCurrency = fromAmount.divide(fromCurrencyRate, 2, RoundingMode.UP)
        val receiveAmount = amountInBaseCurrency.multiply(toCurrencyRate)

        return receiveAmount
    }
}