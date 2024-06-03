package com.example.currencyexchange.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyexchange.data.db.models.RateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rate: List<RateEntity>)

    @Query("""
        select currency,rate 
        from RateEntity
        intersect
        select currency,rate 
        from BalanceEntity
        inner join RateEntity using (currency)
    """)
    // select only those rates in which currency user has an account
    fun getRatesForMyCurrencies(): Flow<List<RateEntity>>
}