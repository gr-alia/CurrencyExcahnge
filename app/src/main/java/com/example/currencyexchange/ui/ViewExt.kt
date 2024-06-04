package com.example.currencyexchange.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun TextView.textFlow(): Flow<CharSequence> = callbackFlow {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            channel.trySend(s)
        }


    }
    addTextChangedListener(textWatcher)
    channel.trySend(text)

    awaitClose {
        removeTextChangedListener(textWatcher)
    }
}

fun Spinner.spinnerFlow(): Flow<String> = callbackFlow {
    val listener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item = parent?.getItemAtPosition(position) as? String
            item?.let {
                channel.trySend(it)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

    }

    onItemSelectedListener = listener
    val initiallySelectedItem = selectedItem as? String
    initiallySelectedItem?.let {
        channel.trySend(it)
    }

    awaitClose {
        onItemSelectedListener = null
    }
}