package com.example.currencyexchange.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.currencyexchange.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var balancesAdapter: BalancesAdapter

    private val currenciesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBalances()
        setupCurrencyDropdowns()

        val sellInput = findViewById<EditText>(R.id.sell_input_edit)
        val receiveInput = findViewById<EditText>(R.id.receive_input_edit)

        sellInput.textFlow()
            .map { it.toBigDecimal() }
            .onEach { viewModel.sellAmount.value = it }
            .onEach {
                if (sellInput.hasFocus()) {
                    val amountToReceive = viewModel.onSellChanged()
                    amountToReceive?.let {
                        receiveInput.setText(it.formatAmount())
                    }
                }
            }
            .launchIn(lifecycleScope)

        receiveInput.textFlow()
            .map { it.toBigDecimal() }
            .onEach { viewModel.receiveAmount.value = it }
            .onEach {
                if (receiveInput.hasFocus()) {
                    val amountToSell = viewModel.onReceiveChanged()
                    amountToSell?.let {
                        sellInput.setText(it.toPlainString())
                    }
                }
            }
            .launchIn(lifecycleScope)

        val sellSpinner = findViewById<Spinner>(R.id.sell_currencies_spinner)
        val receiveSpinner = findViewById<Spinner>(R.id.receive_currencies_spinner)

        sellSpinner.spinnerFlow()
            .onEach { viewModel.sellCurrency.value = it }
            .onEach {
                val amountToReceive = viewModel.onSellChanged()
                amountToReceive?.let {
                    receiveInput.setText(it.formatAmount())
                }
            }
            .launchIn(lifecycleScope)

        receiveSpinner.spinnerFlow()
            .onEach { viewModel.receiveCurrency.value = it }
            .onEach {
                val amountToSell = viewModel.onReceiveChanged()
                amountToSell?.let {
                    sellInput.setText(it.toPlainString())
                }
            }
            .launchIn(lifecycleScope)

        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener { exchangeCurrency() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isButtonEnabled.collectLatest {
                    submitButton.isEnabled = it
                }
            }
        }
    }

    private fun exchangeCurrency() {
        viewModel.submitExchange()
        buildSuccessDialog(this).show()
    }

    private fun setupBalances() {
        val viewPager = findViewById<ViewPager2>(R.id.balances_pager)
        viewPager.adapter = balancesAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.balances.collectLatest { balancesAdapter.submitList(it) }
            }
        }
    }

    private fun setupCurrencyDropdowns() {
        val sellAdapter =
            ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, currenciesList)
        val receiveAdapter =
            ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, currenciesList)


        sellAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        receiveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val sellSpinner = findViewById<Spinner>(R.id.sell_currencies_spinner)
        val receiveSpinner = findViewById<Spinner>(R.id.receive_currencies_spinner)
        sellSpinner.adapter = sellAdapter
        receiveSpinner.adapter = receiveAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rates.collectLatest { rates ->
                    Log.d("MainActivity currencies", rates.toString())

                    rates.map { it.currency }.also {
                        currenciesList.clear()
                        currenciesList.addAll(it)
                    }

                    sellAdapter.notifyDataSetChanged()
                    receiveAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun buildSuccessDialog(context: Context): MaterialAlertDialogBuilder {
        val sellInput = findViewById<EditText>(R.id.sell_input_edit)
        val receiveInput = findViewById<EditText>(R.id.receive_input_edit)
        val sellSpinner = findViewById<Spinner>(R.id.sell_currencies_spinner)
        val receiveSpinner = findViewById<Spinner>(R.id.receive_currencies_spinner)

        val sellText = "${sellInput.text.toBigDecimal().formatAmount()} ${sellSpinner.selectedItem}"
        val receiveText =
            "${receiveInput.text.toBigDecimal().formatAmount()} ${receiveSpinner.selectedItem}"
        val feeText = "0"

        val description = resources.getString(
            R.string.currency_exchange_success_description,
            sellText,
            receiveText,
            feeText
        )

        return MaterialAlertDialogBuilder(context)
            .setMessage(description)
            .setTitle(R.string.currency_exchange_success_title)
            .setPositiveButton(R.string.currency_exchange_success_button) { dialog, _ ->
                dialog.dismiss()
            }.also { it.create() }
    }
}