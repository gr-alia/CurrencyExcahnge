package com.example.currencyexchange.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.data.AccountRepository
import com.example.currencyexchange.data.CurrencyExchangeRepository
import com.example.currencyexchange.domain.ConvertCurrencyUseCase
import com.example.currencyexchange.ui.models.BalanceUIModel
import com.example.currencyexchange.ui.models.toUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    accountRepository: AccountRepository,
    currencyExchangeRepository: CurrencyExchangeRepository,
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            accountRepository.fetchAccount()
            currencyExchangeRepository.fetchRates()
        }
    }

    val balances: StateFlow<List<BalanceUIModel>> =
        accountRepository.getAccountWithBalances()
            .filterNotNull()
            .map { account -> account.balances.map { it.toUIModel() } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

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

    private suspend fun convertCurrency(
        fromAmount: BigDecimal,
        fromCurrency: String,
        toCurrency: String
    ): BigDecimal {
        val fromCurrencyRate = getRateByCurrency(fromCurrency)
        val toCurrencyRate = getRateByCurrency(toCurrency)

        val result = convertCurrencyUseCase.convertCurrency(
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