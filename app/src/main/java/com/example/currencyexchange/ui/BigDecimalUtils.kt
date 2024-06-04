package com.example.currencyexchange.ui

import java.math.BigDecimal
import java.text.DecimalFormat

fun BigDecimal.formatAmount(): String {
    return DecimalFormat("0").apply {
        maximumFractionDigits = 2
    }.format(this)
}

fun CharSequence.toBigDecimal(): BigDecimal = if (isEmpty()) {
    BigDecimal(0)
} else {
    BigDecimal(toString())
}