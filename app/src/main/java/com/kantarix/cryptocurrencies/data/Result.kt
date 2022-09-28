package com.kantarix.cryptocurrencies.data

import com.kantarix.cryptocurrencies.model.Coin
import com.kantarix.cryptocurrencies.model.CoinDetails

sealed class Result {
    class ValidResultCoinsList(val coinsList: List<Coin>) : Result()
    class ValidResultCoinDetails(val coin: CoinDetails) : Result()
    class ErrorResult(val e: Throwable) : Result()
}