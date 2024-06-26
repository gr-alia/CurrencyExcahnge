package com.example.currencyexchange.di

import com.example.currencyexchange.data.db.AppDatabase
import com.example.currencyexchange.data.db.dao.AccountDao
import com.example.currencyexchange.data.db.dao.RatesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbDaoModule {

    @Provides
    fun provideAccountDao(database: AppDatabase): AccountDao = database.accountDao()

    @Provides
    fun provideRatesDao(database: AppDatabase): RatesDao = database.ratesDao()
}