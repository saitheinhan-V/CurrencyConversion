package com.currency.conversion.domain.usecase

import com.currency.conversion.data.database.entity.CurrencyEntity
import com.currency.conversion.data.repository.CurrencyDbRepository
import javax.inject.Inject

class SaveCurrency @Inject constructor(
    private val dbRepository: CurrencyDbRepository
) {
    suspend operator fun invoke(
        currencies: List<CurrencyEntity>
    ){
        dbRepository.saveCurrencies(currencies)
    }
}