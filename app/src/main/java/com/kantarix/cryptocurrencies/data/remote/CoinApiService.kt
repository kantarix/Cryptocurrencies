package com.kantarix.cryptocurrencies.data.remote

import com.kantarix.cryptocurrencies.model.Coin
import com.kantarix.cryptocurrencies.model.CoinDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApiService {

    @GET("coins/markets")
    suspend fun coinsByCurrency(
        @Query("vs_currency") currency: String,
        @Query("page") pageCount: Int,
        @Query("per_page") resultsPerPage: Int
    ) : List<Coin>

    @GET("coins/{id}")
    suspend fun coinDetails(
        @Path("id") id: String
    ) : CoinDetails

}