package com.example.currencyexchange.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.ui.models.BalanceUIModel
import com.example.currencyexchange.ui.models.format
import javax.inject.Inject

class BalancesAdapter @Inject constructor() :
    ListAdapter<BalanceUIModel, BalanceViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder =
        BalanceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_balance_page, parent, false)
        )

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        val balance = getItem(position)

        val amountView = holder.itemView.findViewById<TextView>(R.id.amount_title)
        amountView.text = balance.format()
    }
}

class BalanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

private val diffCallback = object : DiffUtil.ItemCallback<BalanceUIModel>() {
    override fun areItemsTheSame(
        oldItem: BalanceUIModel,
        newItem: BalanceUIModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: BalanceUIModel,
        newItem: BalanceUIModel
    ): Boolean {
        return oldItem == newItem
    }
}