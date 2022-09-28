package com.kantarix.cryptocurrencies.presentation.coinslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kantarix.cryptocurrencies.data.CoinsRepository
import com.kantarix.cryptocurrencies.data.Result
import com.kantarix.cryptocurrencies.model.Coin
import com.kantarix.cryptocurrencies.model.CoinDetails
import com.kantarix.cryptocurrencies.presentation.ViewState
import kotlinx.coroutines.launch

class CoinsViewModel(private val coinsRepo: CoinsRepository) : ViewModel() {

    var currentCurrency = USD

    private val _coinsList = MutableLiveData<List<Coin>>(emptyList())
    val coinsList: LiveData<List<Coin>> get() = _coinsList

    private val _coin = MutableLiveData<CoinDetails>()
    val coin: LiveData<CoinDetails> get() = _coin

    private val _state = MutableLiveData<ViewState>(ViewState.Result)
    val state: LiveData<ViewState> get() = _state

    fun loadCoins() {
        viewModelScope.launch {
            _state.postValue(ViewState.Loading)

            when (val result = coinsRepo.loadCoins(currentCurrency, PAGE, PERPAGE)) {
                is Result.ValidResultCoinsList -> {
                    _coinsList.postValue(result.coinsList)
                    _state.postValue(ViewState.Result)
                }
                is Result.ErrorResult -> {
                    _coinsList.postValue(emptyList())
                    _state.postValue(ViewState.Error(result.e))
                }
                else -> {
                    _state.postValue(ViewState.Error(Throwable("Something went wrong.")))
                }
            }
        }
    }

    fun loadCoin(id: String) {
        viewModelScope.launch {
            _state.postValue(ViewState.Loading)

            when (val result = coinsRepo.loadCoinDetails(id)) {
                is Result.ValidResultCoinDetails -> {
                    _coin.postValue(result.coin)
                    _state.postValue(ViewState.Result)
                }
                is Result.ErrorResult -> {
                    _state.postValue(ViewState.Error(result.e))
                }
                else -> {
                    _state.postValue(ViewState.Error(Throwable("Something went wrong.")))
                }
            }
        }
    }

    companion object {
        private const val PAGE = 1
        private const val PERPAGE = 30
        const val USD = "usd"
    }
}