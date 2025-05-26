package ru.volatex.volatexmobile.presentation.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.volatex.volatexmobile.databinding.ItemPortfolioCardBinding
import ru.volatex.volatexmobile.domain.model.Position

class PortfolioAdapter(private val positions: List<Position>) :
    RecyclerView.Adapter<PortfolioAdapter.PortfolioViewHolder>() {

    inner class PortfolioViewHolder(val binding: ItemPortfolioCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPortfolioCardBinding.inflate(inflater, parent, false)
        return PortfolioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        val item = positions[position]
        holder.binding.apply {
            nameText.text = item.name
            quantityText.text = "Количество: ${item.quantity}"
            valueText.text = "Текущая стоимость: ${(item.current_price - item.commission).format(2)}₽"
            volatilityText.text = "Волатильность: ${item.volatility}"
        }
    }

    override fun getItemCount(): Int = positions.size

    private fun Double.format(decimals: Int): String = "%.${decimals}f".format(this)
}
