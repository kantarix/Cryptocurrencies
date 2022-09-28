package com.kantarix.cryptocurrencies.presentation

sealed class ViewState {
    object Result : ViewState()
    object Loading : ViewState()
    class Error(val e: Throwable) : ViewState()
}