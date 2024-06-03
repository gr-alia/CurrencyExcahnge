package com.example.currencyexchange.data

import com.example.currencyexchange.data.api.AccountRemoteDataSource
import com.example.currencyexchange.data.db.dao.AccountDao
import com.example.currencyexchange.data.db.models.AccountWithBalances
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountRemoteDataSource,
    private val accountDao: AccountDao
) : AccountRepository {
    override suspend fun fetchAccount() {
        val accountDTO = remoteDataSource.getAccount()
        accountDao.insertAccountWithBalances(accountDTO)
    }

    override fun getAccountWithBalances(): Flow<AccountWithBalances?> {
        return accountDao.getAccountWithBalances()
    }
}