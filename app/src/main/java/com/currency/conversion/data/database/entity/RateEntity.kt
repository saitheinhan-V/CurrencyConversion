package com.currency.conversion.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.currency.conversion.domain.model.Rate

@Entity(
    tableName = RateEntity.RATE_TABLE,
)
data class RateEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val source: String,
    val code: String,
    val amount: Double,
    val timestamp: Long,
){
    companion object {
        const val RATE_TABLE = "rate_room_table"
        const val RATE_AMOUNT = "amount"
        const val RATE_SOURCE = "source"
        const val RATE_CODE = "code"
        const val RATE_TIMESTAMP = "timestamp"
    }

    fun toVo(): Rate {
        return Rate(
            source = source,
            key = code,
            amount = amount,
            timestamp = timestamp
        )
    }
}