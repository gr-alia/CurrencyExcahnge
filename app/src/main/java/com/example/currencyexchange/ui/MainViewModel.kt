package com.example.currencyexchange.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchange.data.AccountRepository
import com.example.currencyexchange.data.CurrencyExchangeRepository
import com.example.currencyexchange.ui.models.BalanceUIModel
import com.example.currencyexchange.ui.models.toUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    accountRepository: AccountRepository,
    currencyExchangeRepository: CurrencyExchangeRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            accountRepository.fetchAccount()
        }
    }

    val balances: StateFlow<List<BalanceUIModel>> =
        accountRepository.getAccountWithBalances()
            .map { account -> account.balances.map { it.toUIModel() } }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}