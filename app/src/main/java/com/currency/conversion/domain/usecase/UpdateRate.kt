package com.currency.conversion.domain.usecase

import com.currency.conversion.data.repository.CurrencyDbRepository
import javax.inject.Inject

class UpdateRate @Inject constructor(
    private val dbRepo: CurrencyDbRepository
){
    suspend operator fun invoke(
        amount: Double,
        source: String,
        code: String,
        timestamp: Long
    ){
        dbRepo.updateRates(amount, source,code, timestamp)
    }
}