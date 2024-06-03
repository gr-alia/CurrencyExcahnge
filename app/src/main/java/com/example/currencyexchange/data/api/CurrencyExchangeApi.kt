package com.example.currencyexchange.data.api

import com.example.currencyexchange.data.api.models.RatesResponse
import retrofit2.http.GET

interface CurrencyExchangeApi {

    @GET("tasks/api/currency-exchange-rates")
    suspend fun getRates(): RatesResponse
}