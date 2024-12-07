package com.currency.conversion.domain.usecase

import com.currency.conversion.data.repository.CurrencyDbRepository
import com.currency.conversion.domain.model.Rate
import javax.inject.Inject

class GetRate @Inject constructor(
    private val dbRepo: CurrencyDbRepository
){
    suspend operator fun invoke(
        source: String,
    ): List<Rate>{
        return dbRepo.getRates(source).map { data -> data.toVo() }
    }
}