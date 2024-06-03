package com.example.currencyexchange.di

import com.example.currencyexchange.data.api.CurrencyExchangeApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl("https://developers.paysera.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideCurrencyExchangeApi(retrofit: Retrofit): CurrencyExchangeApi = retrofit.create()
}