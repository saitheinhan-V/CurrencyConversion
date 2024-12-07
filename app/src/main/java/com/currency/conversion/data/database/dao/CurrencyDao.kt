package com.currency.conversion.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.currency.conversion.data.database.entity.CurrencyEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)

    @Query("DELETE FROM ${CurrencyEntity.CURRENCY_TABLE}")
    suspend fun deleteCurrencies()

    @Query("SELECT * FROM ${CurrencyEntity.CURRENCY_TABLE}")
    fun readCurrencies(): List<CurrencyEntity>

}
