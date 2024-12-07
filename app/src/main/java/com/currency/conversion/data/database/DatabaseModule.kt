package com.currency.conversion.data.database

import android.content.Context
import androidx.room.Room
import com.currency.conversion.data.database.dao.CurrencyDao
import com.currency.conversion.data.database.dao.RateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): CurrencyDatabase {
        return Room
            .databaseBuilder(
                context,
                CurrencyDatabase::class.java,
                CurrencyDatabase.DB_NAME
            )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideCurrencyDao(
        database: CurrencyDatabase
    ): CurrencyDao{
        return database.currencyDao()
    }

    @Provides
    @Singleton
    fun provideRateDao(
        database: CurrencyDatabase
    ): RateDao{
        return database.rateDao()
    }

}