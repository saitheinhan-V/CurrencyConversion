package com.currency.conversion.presentation.utils

import java.text.DecimalFormat

fun decimalSeparator(value : Double) : String{
    return DecimalFormat("#,###,###,##0.00000").format(value)
}