package com.currency.conversion.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currency.conversion.data.database.entity.CurrencyEntity
import com.currency.conversion.data.database.entity.RateEntity
import com.currency.conversion.data.network.NetworkResponse
import com.currency.conversion.data.repository.CurrencyDbRepository
import com.currency.conversion.domain.model.Currency
import com.currency.conversion.domain.model.CurrencyListDTO
import com.currency.conversion.domain.model.Rate
import com.currency.conversion.domain.usecase.MainUseCase
import com.currency.conversion.domain.vo.CurrencyListVo
import com.currency.conversion.domain.vo.CurrencyRateVo
import com.currency.conversion.presentation.event.MainEvent
import com.currency.conversion.presentation.state.Status
import com.currency.conversion.presentation.state.ViewState
import com.currency.conversion.presentation.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MainUseCase,
    private val networkHelper: NetworkHelper,
) : ViewModel() {

    private val refreshInterval = 30 * 60 // 30 minutes in seconds

    private val vmEvent = MutableSharedFlow<MainEvent>()
    val uiEvent get() = vmEvent.asSharedFlow()

    private var currencyList: MutableList<Currency> = mutableListOf()
    private var currencyEntityList: MutableList<CurrencyEntity> = mutableListOf()
    private var rateList: MutableList<Rate> = mutableListOf()
    private var rateEntityList: MutableList<RateEntity> = mutableListOf()

    val currencyListState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            mutableListOf<Currency>(),
            "Loading..."
        )
    )

    val currencyRateState = MutableStateFlow(
        ViewState(
            Status.LOADING,
            mutableListOf<Rate>(),
            "Loading..."
        )
    )

    //for Rate List
    fun fetchCurrencyRate(source: String) {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                vmEvent.emit(MainEvent.showSnack("No internet connection!"))
                return@launch
            }

            useCase.getRate(
                source = source
            ).also { rates ->

                if (rates.isEmpty()) {
                    fetchRateAPI(source, false)
                } else {
                    val now = System.currentTimeMillis() / 1000
                    //timestamp from API is in seconds format
                    if ((now - rates[0].timestamp) > refreshInterval) {
                        fetchRateAPI(source, true)
                    } else {
                        Log.i("rate", "Local => ${rates.size} $rates")
                        currencyRateState.value = ViewState.success(rates.toMutableList())
                    }
                }
            }
        }
    }

    //for Rate List API Call
    private fun fetchRateAPI(source: String, updated: Boolean) {
        viewModelScope.launch {
            currencyRateState.value = ViewState.loading()

            useCase.liveCurrencyRate(
                source = source
            ).collectLatest {
                when (it) {
                    is NetworkResponse.Fail -> {
                        Log.i("rate", "Fail")
                        currencyRateState.value = ViewState.error(it.message)
                    }

                    is NetworkResponse.Success -> {
                        Log.i("rate", "Success -> ${it.data}")

                        if (it.data.success!!) {
                            val requestTimestamp = it.data.timestamp!!
                            val map: Map<String, Double> = it.data.quotes!!

                            val keys: Array<String> = map.keys.toTypedArray()
                            val values: Array<Double> = map.values.toTypedArray()

                            rateEntityList = mutableListOf()
                            rateList = mutableListOf()
                            for (row in keys.indices) {
                                val rateEntity = RateEntity(
                                    source = source,
                                    code = keys[row],
                                    amount = values[row],
                                    timestamp = requestTimestamp
                                )
                                rateList.add(rateEntity.toVo())
                                rateEntityList.add(rateEntity)
                            }

                            //room db
                            if (updated) {
                                //update data which is more than 30 min
                                Log.i("roomUpdate", "RoomUpdate => $requestTimestamp $source")
                                for (rate in rateList) {
                                    useCase.updateRate(
                                        amount = rate.amount,
                                        source = rate.source,
                                        code = rate.key,
                                        timestamp = requestTimestamp
                                    )
                                }
                            } else {
                                //only save new data to room db
                                useCase.saveRate(rateEntityList.toList())
                            }

                            currencyRateState.value = ViewState.success(rateList)
                        } else {
                            currencyRateState.value = ViewState.error("")
                        }

                    }
                }
            }
        }
    }

    //for Currency List API Call
    fun fetchCurrencyList() {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                vmEvent.emit(MainEvent.showSnack("No internet connection!"))
                return@launch
            }

            useCase.getCurrency().also { currencies ->
                if (currencies.isNotEmpty()) { //get from room db
                    currencyListState.value = ViewState.success(currencies.toMutableList())
                } else {
                    //save to room db
                    currencyListState.value = ViewState.loading()

                    useCase.allCurrencyList().collectLatest {
                        when (it) {
                            is NetworkResponse.Fail -> {
                                Log.i("list", "Fail")
                                currencyListState.value = ViewState.error(it.message)
                            }

                            is NetworkResponse.Success -> {
                                Log.i("list", "List -> ${it.data}")
                                if (it.data.success!!) {
                                    val map: Map<String, String> = it.data.currencies!!

                                    val keys: Array<String> = map.keys.toTypedArray()
                                    val values: Array<String> = map.values.toTypedArray()

                                    currencyList = mutableListOf()
                                    currencyEntityList = mutableListOf()
                                    for (i in keys.indices) {
                                        val entity = CurrencyEntity(keys[i], values[i])
                                        currencyEntityList.add(entity)
                                        currencyList.add(entity.toVo())
                                    }

                                    //save to room db
                                    useCase.saveCurrency(currencies = currencyEntityList.toList())

                                    currencyListState.value = ViewState.success(currencyList)
                                } else {
                                    currencyListState.value = ViewState.error("")
                                }

                            }
                        }
                    }

                }
            }
        }
    }
}