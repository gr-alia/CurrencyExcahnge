package com.example.currencyexchange.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.View
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

fun TextView.focusFlow(): Flow<Boolean> = callbackFlow {
    val focusListener = View.OnFocusChangeListener { _, hasFocus ->
        channel.trySend(hasFocus)
    }
    onFocusChangeListener = focusListener
    channel.trySend(hasFocus())

    awaitClose {
        onFocusChangeListener = null
    }
}