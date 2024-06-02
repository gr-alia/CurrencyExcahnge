package com.example.currencyexchange.di

import com.example.currencyexchange.data.AccountRepository
import com.example.currencyexchange.data.AccountRepositoryImpl
import com.example.currencyexchange.data.CurrencyExchangeRepository
import com.example.currencyexchange.data.CurrencyExchangeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    abstract fun bindCurrencyExchangeRepository(
        currencyExchangeRepositoryImpl: CurrencyExchangeRepositoryImpl
    ): CurrencyExchangeRepository
}