package com.currency.conversion.presentation.event

sealed interface MainEvent {

    data class showSnack(val message: String) : MainEvent
}