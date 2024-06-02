package com.example.currencyexchange.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.currencyexchange.data.api.models.AccountDTO
import com.example.currencyexchange.data.db.models.AccountEntity
import com.example.currencyexchange.data.db.models.AccountWithBalances
import com.example.currencyexchange.data.db.models.BalanceEntity
import com.example.currencyexchange.data.mappers.toEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalance(balance: BalanceEntity)

    @Transaction
    suspend fun insertAccountWithBalances(accountDTO: AccountDTO) {
        insertAccount(accountDTO.toEntity())
        accountDTO.balances.forEach {
            insertBalance(it.toEntity(accountDTO.id))
        }
    }

    @Transaction
    @Query("select * from AccountEntity")
    fun getAccountWithBalances(): Flow<AccountWithBalances>
}