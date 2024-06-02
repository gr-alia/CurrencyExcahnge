package com.example.currencyexchange.ui

import androidx.lifecycle.ViewModel
import com.example.currencyexchange.data.AccountRepository
import com.example.currencyexchange.data.CurrencyExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    accountRepository: AccountRepository,
    currencyExchangeRepository: CurrencyExchangeRepository
): ViewModel() {
}