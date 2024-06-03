package com.example.currencyexchange.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.data.AccountRepository
import com.example.currencyexchange.data.CurrencyExchangeRepository
import com.example.currencyexchange.ui.models.BalanceUIModel
import com.example.currencyexchange.ui.models.toUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    accountRepository: AccountRepository,
    currencyExchangeRepository: CurrencyExchangeRepository
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

    private val _sellAmount = MutableStateFlow(BigDecimal(0))
    val sellAmount = _sellAmount.asStateFlow()

    private val _receiveAmount = MutableStateFlow(BigDecimal(0))
    val receiveAmount = _receiveAmount.asStateFlow()

    fun updateSellAmount(newAmount: BigDecimal){
        if (newAmount != _sellAmount.value) {
            _sellAmount.value = newAmount
        }
    }

    fun updateReceiveAmount(newAmount: BigDecimal){
        if (newAmount != _receiveAmount.value) {
            _receiveAmount.value = newAmount
        }
    }
}