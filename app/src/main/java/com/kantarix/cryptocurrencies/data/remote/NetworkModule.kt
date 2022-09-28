package com.kantarix.cryptocurrencies.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class NetworkModule(noConnectionInterceptor: NoConnectionInterceptor) {

    private val baseUrl = "https://api.coingecko.com/api/"
    private val version = "v3/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val contentType = "application/json".toMediaType()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(noConnectionInterceptor)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl + version)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(httpClient)
        .build()

    val api: CoinApiService by lazy { retrofit.create(CoinApiService::class.java) }

}