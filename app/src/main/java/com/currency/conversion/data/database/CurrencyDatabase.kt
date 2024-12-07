package com.currency.conversion.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.currency.conversion.data.database.dao.CurrencyDao
import com.currency.conversion.data.database.dao.RateDao
import com.currency.conversion.data.database.entity.CurrencyEntity
import com.currency.conversion.data.database.entity.RateEntity

@Database(
    entities = [
        CurrencyEntity::class,
        RateEntity::class
    ],
    version = 3
)

abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun rateDao(): RateDao

    companion object {
        const val DB_NAME = "currency_conversion_db"
    }
}
