package com.kantarix.cryptocurrencies.presentation.coinslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kantarix.cryptocurrencies.R
import com.kantarix.cryptocurrencies.model.Coin
import java.util.*
import kotlin.math.abs

class CoinsAdapter(
    var currency: String,
    private val onClickCard: (coin: Coin) -> Unit
) : ListAdapter<Coin, CoinsAdapter.CoinsViewHolder>(CoinsCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CoinsViewHolder(inflater.inflate(R.layout.view_holder_coin, parent, false))
    }

    override fun onBindViewHolder(holder: CoinsViewHolder, position: Int) {
        holder.onBind(getItem(position), onClickCard)
    }

    inner class CoinsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val image: AppCompatImageView = itemView.findViewById(R.id.coin_image)
        private val name: AppCompatTextView = itemView.findViewById(R.id.coin_name)
        private val symbol: AppCompatTextView = itemView.findViewById(R.id.coin_symbol)
        private val price: AppCompatTextView = itemView.findViewById(R.id.coin_price)
        private val changePercentage: AppCompatTextView = itemView.findViewById(R.id.coin_change_percentage)

        fun onBind(coin: Coin, onClickCard: (coin: Coin) -> Unit) {
            Glide.with(itemView.context)
                .load(coin.image)
                .into(image)
            name.text = coin.name
            symbol.text = coin.symbol
            price.text = when (currency) {
                "usd" -> itemView.context.getString(R.string.usd_price).format(Locale.ENGLISH, coin.currentPrice)
                "eur" -> itemView.context.getString(R.string.eur_price).format(Locale.ENGLISH, coin.currentPrice)
                else -> itemView.context.getString(R.string.usd_price).format(Locale.ENGLISH, coin.currentPrice)
            }
            setPercentage(coin.changePercentage24h)

            itemView.setOnClickListener { onClickCard(coin) }
        }

        private fun setPercentage(percentage: Double) {
            when {
                percentage > 0 -> {
                    changePercentage.text = itemView.context.getString(R.string.change_percentage_plus).format(Locale.ENGLISH, abs(percentage))
                    changePercentage.setTextColor(ContextCompat.getColor(itemView.context, R.color.jungle_green))
                }
                percentage < 0 -> {
                    changePercentage.text = itemView.context.getString(R.string.change_percentage_minus).format(Locale.ENGLISH, abs(percentage))
                    changePercentage.setTextColor(ContextCompat.getColor(itemView.context, R.color.burnt_sienna))
                }
                else -> {
                    changePercentage.text = itemView.context.getString(R.string.change_percentage_minus).format(Locale.ENGLISH, abs(percentage))
                }
            }
        }
    }

}

class CoinsCallback : DiffUtil.ItemCallback<Coin>() {
    override fun areItemsTheSame(oldItem: Coin, newItem: Coin) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Coin, newItem: Coin) =
        oldItem == newItem
}