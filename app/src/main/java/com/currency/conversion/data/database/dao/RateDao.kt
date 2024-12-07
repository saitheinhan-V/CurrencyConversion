package com.currency.conversion.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.currency.conversion.data.database.entity.CurrencyEntity
import com.currency.conversion.data.database.entity.RateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<RateEntity>)

    @Query("DELETE FROM ${RateEntity.RATE_TABLE} WHERE ${RateEntity.RATE_SOURCE} = :code")
    suspend fun deleteRates(code: String)

    @Query("SELECT * FROM ${RateEntity.RATE_TABLE} WHERE ${RateEntity.RATE_SOURCE} = :source")
    fun readRates(
        source: String,
    ): List<RateEntity>

    @Query(
        "UPDATE ${RateEntity.RATE_TABLE} SET " +
                "${RateEntity.RATE_AMOUNT} = :amount AND " +
                "${RateEntity.RATE_TIMESTAMP} = :timestamp " +
                "WHERE ${RateEntity.RATE_SOURCE} = :source AND " +
                "${RateEntity.RATE_CODE} = :code"
    )
    fun updateRates(
        amount: Double,
        source: String,
        code: String,
        timestamp: Long
    )
}
