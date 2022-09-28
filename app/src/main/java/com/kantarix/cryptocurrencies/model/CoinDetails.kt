package com.kantarix.cryptocurrencies.model

import kotlinx.serialization.Serializable

@Serializable
data class CoinDetails (
    val name: String,
    val description: Descriptions,
    val categories: List<String>,
    val image: Images
)

@Serializable
data class Descriptions (
    val en: String
)

@Serializable
data class Images (
    val large: String
)