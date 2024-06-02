package com.example.currencyexchange.di

import android.content.Context
import androidx.room.Room
import com.example.currencyexchange.data.db.AppDatabase
import com.example.currencyexchange.data.db.ModelConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    fun provideDatabase(
        @ApplicationContext
        applicationContext: Context,
        modelConverter: ModelConverter
    ): AppDatabase = Room
        .databaseBuilder(applicationContext, AppDatabase::class.java, "app-main-database")
        .addTypeConverter(modelConverter)
        .build()
}