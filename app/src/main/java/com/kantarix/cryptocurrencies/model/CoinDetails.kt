package com.kantarix.cryptocurrencies.model

data class CoinDetails (
    val name: String,
    val description: Descriptions,
    val categories: List<String>,
    val image: Images
)

data class Descriptions (
    val en: String
)

data class Images (
    val large: String
)