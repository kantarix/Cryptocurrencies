package com.kantarix.cryptocurrencies.data

import com.kantarix.cryptocurrencies.data.remote.CoinApiService
import com.kantarix.cryptocurrencies.model.Coin
import com.kantarix.cryptocurrencies.model.CoinDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoinsRepository(private val api: CoinApiService) {

    suspend fun loadCoins(currency: String, pageCount: Int, resultsPerPage: Int): Result {
        return withContext(Dispatchers.IO) {
            val coins: List<Coin>
            try {
                coins = api.coinsByCurrency(currency, pageCount, resultsPerPage)
                Result.ValidResultCoinsList(coins)
            } catch (e: Throwable) {
                Result.ErrorResult(e)
            }
        }
    }

    suspend fun loadCoinDetails(id: String): Result {
        return withContext(Dispatchers.IO) {
            val coin: CoinDetails
            try {
                coin = api.coinDetails(id)
                Result.ValidResultCoinDetails(coin)
            } catch (e: Throwable) {
                Result.ErrorResult(e)
            }
        }
    }
}