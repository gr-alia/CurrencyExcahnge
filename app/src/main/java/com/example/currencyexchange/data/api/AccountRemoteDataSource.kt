package com.example.currencyexchange.data.api

import com.example.currencyexchange.data.api.models.AccountDTO

interface AccountRemoteDataSource {

    suspend fun getAccount(): AccountDTO
}