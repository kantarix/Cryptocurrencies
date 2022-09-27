package com.kantarix.cryptocurrencies.model

data class Coin (
    val id: String,
    val name: String,
    val symbol: String,
    val image: String,
    val currentPrice: Double,
    val changePercentage24h: Double
)