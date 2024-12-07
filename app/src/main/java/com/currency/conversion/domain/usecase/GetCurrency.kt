package com.currency.conversion.domain.usecase

import com.currency.conversion.data.repository.CurrencyDbRepository
import com.currency.conversion.domain.model.Currency
import javax.inject.Inject

class GetCurrency @Inject constructor(
    private val dbRepository: CurrencyDbRepository
){
    suspend operator fun invoke(): List<Currency>{
        return dbRepository.getCurrencies().map { data -> data.toVo() }
    }
}