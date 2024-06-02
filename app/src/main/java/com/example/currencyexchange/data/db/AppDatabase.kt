package com.example.currencyexchange.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.currencyexchange.data.db.dao.AccountDao
import com.example.currencyexchange.data.db.models.AccountEntity
import com.example.currencyexchange.data.db.models.BalanceEntity

@Database(entities = [AccountEntity::class, BalanceEntity::class], version = 1)
@TypeConverters(
    value = [ModelConverter::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
}