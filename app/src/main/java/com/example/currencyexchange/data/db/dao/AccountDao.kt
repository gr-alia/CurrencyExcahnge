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
import java.math.BigDecimal

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
    fun getAccountWithBalances(): Flow<AccountWithBalances?>

    @Query("select * from BalanceEntity")
    fun getBalances(): Flow<List<BalanceEntity>>

    @Query("select * from BalanceEntity where currency = :currency")
    suspend fun getBalanceByCurrency(currency: String): BalanceEntity

    @Transaction
    suspend fun updateTransactionalBalances(
        fromBalance: BigDecimal,
        fromCurrency: String,
        toBalance: BigDecimal,
        toCurrency: String
    ) {
        updateBalance(fromBalance, fromCurrency)
        updateBalance(toBalance, toCurrency)
    }

    @Query("update BalanceEntity set amount = :newAmount where currency = :currency")
    suspend fun updateBalance(newAmount: BigDecimal, currency: String)
}