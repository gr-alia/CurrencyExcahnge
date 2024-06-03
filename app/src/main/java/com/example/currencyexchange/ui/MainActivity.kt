package com.example.currencyexchange.ui

import android.os.Bundle
import android.util.Log
import android.widget.EditText
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var balancesAdapter: BalancesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewPager = findViewById<ViewPager2>(R.id.balances_pager)
        viewPager.adapter = balancesAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.balances.collectLatest { balancesAdapter.submitList(it) }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.currencies.collectLatest {
                    Log.d("MainActivity currencies", it.toString())
                }
            }
        }

        val sellInput = findViewById<EditText>(R.id.sell_input_edit)
        val receiveInput = findViewById<EditText>(R.id.receive_input_edit)

        sellInput.textFlow()
            .map { it.toBigDecimal() }
            .onEach { viewModel.updateSellAmount(it) }
            .launchIn(lifecycleScope)

        receiveInput.textFlow()
            .map { it.toBigDecimal() }
            .onEach { viewModel.updateReceiveAmount(it) }
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sellAmount.collectLatest {
                    Log.d("MainActivity sellAmount", it.toString())
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.receiveAmount.collectLatest {
                    Log.d("MainActivity receiveAmount", it.toString())
                }
            }
        }

    }
}

private fun CharSequence.toBigDecimal(): BigDecimal = if (isEmpty()) {
    BigDecimal(0)
} else {
    BigDecimal(toString())
}