package com.example.currencyexchange.data

import com.example.currencyexchange.data.db.models.AccountWithBalances
import com.example.currencyexchange.data.db.models.BalanceEntity
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    suspend fun fetchAccount()

    fun getAccountWithBalances(): Flow<AccountWithBalances?>

    fun getBalances(): Flow<List<BalanceEntity>>
}