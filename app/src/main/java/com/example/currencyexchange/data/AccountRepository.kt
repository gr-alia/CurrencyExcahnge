package com.example.currencyexchange.data

import com.example.currencyexchange.data.db.models.AccountWithBalances
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun fetchAccount()

    fun getAccountWithBalances(): Flow<AccountWithBalances>
}