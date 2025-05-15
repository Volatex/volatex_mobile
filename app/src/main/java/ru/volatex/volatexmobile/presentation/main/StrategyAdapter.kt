package ru.volatex.volatexmobile.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.volatex.volatexmobile.R
import ru.volatex.volatexmobile.domain.model.Strategy

class StrategyAdapter : RecyclerView.Adapter<StrategyAdapter.StrategyViewHolder>() {

    private var strategies: List<Strategy> = emptyList()

    fun submitList(list: List<Strategy>) {
        strategies = list
        notifyDataSetChanged()
    }

    class StrategyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val figiText: TextView = itemView.findViewById(R.id.figiText)
        val buyPriceText: TextView = itemView.findViewById(R.id.buyInfoText)
        val sellPriceText: TextView = itemView.findViewById(R.id.sellInfoText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StrategyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.strategy_card, parent, false)
        return StrategyViewHolder(view)
    }

    override fun onBindViewHolder(holder: StrategyViewHolder, position: Int) {
        val strategy = strategies[position]
        holder.figiText.text = strategy.Figi
        holder.buyPriceText.text = "Покупка: ${strategy.BuyPrice} x ${strategy.BuyQuantity}"
        holder.sellPriceText.text = "Продажа: ${strategy.SellPrice} x ${strategy.SellQuantity}"
    }

    override fun getItemCount() = strategies.size
}
