package com.kantarix.cryptocurrencies.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coin (
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    @SerialName("current_price")
    val currentPrice: Double,
    @SerialName("price_change_percentage_24h")
    val changePercentage24h: Double
)