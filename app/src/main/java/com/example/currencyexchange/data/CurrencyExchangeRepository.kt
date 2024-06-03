package com.example.currencyexchange.data

interface CurrencyExchangeRepository {

    suspend fun fetchRates()
}