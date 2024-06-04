package com.example.currencyexchange.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.data.AccountRepository
import com.example.currencyexchange.data.CurrencyExchangeRepository
import com.example.currencyexchange.domain.CalculateCurrencyConversionUseCase
import com.example.currencyexchange.ui.models.BalanceUIModel
import com.example.currencyexchange.ui.models.toUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    accountRepository: AccountRepository,
    private val currencyExchangeRepository: CurrencyExchangeRepository,
    private val conversionUseCase: CalculateCurrencyConversionUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            accountRepository.fetchAccount()
            currencyExchangeRepository.fetchRates()
        }
    }

    val balances: Flow<List<BalanceUIModel>> =
        accountRepository.getBalances()
            .map { list -> list.map { it.toUIModel() } }
           // .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val rates = currencyExchangeRepository.getRatesForMyCurrencies()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val sellAmount: MutableStateFlow<BigDecimal?> = MutableStateFlow(null)

    val receiveAmount: MutableStateFlow<BigDecimal?> = MutableStateFlow(null)

    val sellCurrency: MutableStateFlow<String?> = MutableStateFlow(null)

    val receiveCurrency: MutableStateFlow<String?> = MutableStateFlow(null)

    suspend fun onSellChanged(): BigDecimal? {
        val fromAmount = sellAmount.value ?: return null
        val fromCurrency = sellCurrency.value ?: return null
        val toCurrency = receiveCurrency.value ?: return null

        return convertCurrency(fromAmount, fromCurrency, toCurrency)
    }

    suspend fun onReceiveChanged(): BigDecimal? {
        val fromAmount = receiveAmount.value ?: return null
        val fromCurrency = receiveCurrency.value ?: return null
        val toCurrency = sellCurrency.value ?: return null

        return convertCurrency(fromAmount, fromCurrency, toCurrency)
    }

    fun submitExchange() {
        val fromAmount = sellAmount.value ?: return
        val fromCurrency = sellCurrency.value ?: return
        val toAmount = receiveAmount.value ?: return
        val toCurrency = receiveCurrency.value ?: return

        viewModelScope.launch {
            currencyExchangeRepository.exchangeCurrency(
                fromAmount,
                fromCurrency,
                toAmount,
                toCurrency
            )
        }
    }

    private suspend fun convertCurrency(
        fromAmount: BigDecimal,
        fromCurrency: String,
        toCurrency: String
    ): BigDecimal {
        val fromCurrencyRate = getRateByCurrency(fromCurrency)
        val toCurrencyRate = getRateByCurrency(toCurrency)

        val result = conversionUseCase.calculateConversion(
            fromAmount,
            fromCurrencyRate,
            toCurrencyRate
        )

        return result
    }

    private suspend fun getRateByCurrency(currency: String): BigDecimal {
        val rate = rates.first().firstOrNull { it.currency.equals(currency, ignoreCase = true) }
        if (rate == null) throw IllegalStateException("Rates list must have appropriate currency")
        return rate.rate
    }
}