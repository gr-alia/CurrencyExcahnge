package com.example.currencyexchange.ui.models

import com.example.currencyexchange.data.db.models.BalanceEntity
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

data class BalanceUIModel(
    val id: String,
    val amount: BigDecimal,
    val currency: Currency
)

fun BalanceEntity.toUIModel(): BalanceUIModel = BalanceUIModel(id, amount, Currency.getInstance(id))

fun BalanceUIModel.format(locale: Locale = Locale.getDefault()): String {
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    numberFormat.currency = this.currency

    return numberFormat.format(this.amount)
}