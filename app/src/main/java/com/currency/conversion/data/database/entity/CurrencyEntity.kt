package com.currency.conversion.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.currency.conversion.data.database.entity.CurrencyEntity.Companion.CURRENCY_TABLE
import com.currency.conversion.domain.model.Currency

@Entity(
    tableName = CURRENCY_TABLE,
)
data class CurrencyEntity(
    @PrimaryKey val key: String, // e.g., "AED", "AFN", etc.
    val name: String
) {
    companion object {
        const val CURRENCY_TABLE = "currency_room_table"
    }

    fun toVo(): Currency {
        return Currency(
            key = key,
            name = name
        )
    }
}
