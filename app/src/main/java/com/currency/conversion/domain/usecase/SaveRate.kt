package com.currency.conversion.domain.usecase

import com.currency.conversion.data.database.entity.RateEntity
import com.currency.conversion.data.repository.CurrencyDbRepository
import com.currency.conversion.domain.model.Rate
import javax.inject.Inject

class SaveRate @Inject constructor(
    private val dbRepo: CurrencyDbRepository
){
    suspend operator fun invoke(rates: List<RateEntity>){
        dbRepo.saveRates(rates)
    }
}