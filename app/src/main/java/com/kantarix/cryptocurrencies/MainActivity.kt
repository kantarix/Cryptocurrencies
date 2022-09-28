package com.kantarix.cryptocurrencies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kantarix.cryptocurrencies.presentation.coin.CoinDetailsFragment
import com.kantarix.cryptocurrencies.presentation.coinslist.CoinsListFragment

class MainActivity : AppCompatActivity(),
    CoinsListFragment.CoinItemClickListener,
    BackClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            routeToCoinsList()
        }
    }

    override fun onCoinClick(coinId: String) {
        routeToCoinDetails(coinId)
    }

    override fun onBackClick() {
        supportFragmentManager.popBackStack()
    }

    private fun routeToCoinsList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, CoinsListFragment.newInstance(), FRAGMENT_COINS_LIST)
            .commit()
    }

    private fun routeToCoinDetails(coinId: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, CoinDetailsFragment.newInstance(coinId), FRAGMENT_COIN_DETAILS)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val FRAGMENT_COINS_LIST = "coins_list"
        private const val FRAGMENT_COIN_DETAILS = "coin_details"
    }

}

interface BackClickListener {
    fun onBackClick()
}