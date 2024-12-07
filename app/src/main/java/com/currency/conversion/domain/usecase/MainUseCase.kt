package com.currency.conversion.domain.usecase

import javax.inject.Inject

class MainUseCase @Inject constructor(
    val allCurrencyList: CurrencyList,
    val liveCurrencyRate: CurrencyRates,
    val saveCurrency: SaveCurrency,
    val getCurrency: GetCurrency,

    val saveRate: SaveRate,
    val updateRate: UpdateRate,
    val getRate: GetRate
)