package com.kantarix.cryptocurrencies.presentation.coinslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.kantarix.cryptocurrencies.R
import com.kantarix.cryptocurrencies.data.CoinsRepository
import com.kantarix.cryptocurrencies.data.remote.CoinApiService
import com.kantarix.cryptocurrencies.data.remote.NetworkModule
import com.kantarix.cryptocurrencies.data.remote.NoConnectionInterceptor
import com.kantarix.cryptocurrencies.model.Coin

class CoinsListFragment : Fragment() {

    private var rvCoins: RecyclerView? = null
    private lateinit var coinsAdapter: CoinsAdapter
    private var listener: CoinItemClickListener? = null

    private var chipUsd: Chip? = null
    private var chipEur: Chip? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CoinItemClickListener)
            listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_coins_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpListeners()
        setUpCoinsAdapter()

    }

    override fun onDestroy() {
        super.onDestroy()
        destroyViews()
    }

    private fun initViews() {
        chipUsd = view?.findViewById(R.id.chip_usd)
        chipEur = view?.findViewById(R.id.chip_eur)
        rvCoins = view?.findViewById(R.id.coins_list_rv)
    }

    private fun setUpListeners() {
        chipUsd?.setOnClickListener {
            val currency = chipUsd?.text ?: CoinsViewModel.USD
        }
        chipEur?.setOnClickListener {
            val currency = chipEur?.text ?: CoinsViewModel.USD
        }
    }

    private fun setUpCoinsAdapter() {
        coinsAdapter = CoinsAdapter("usd") { coin ->
            listener?.onCoinClick(coinId = coin.id)
        }
        rvCoins?.adapter = coinsAdapter
        rvCoins?.setHasFixedSize(true)
    }

    private fun destroyViews() {
        rvCoins = null
        chipUsd = null
        chipEur = null
    }

    companion object {
        fun newInstance() = CoinsListFragment()

        private const val usd = "usd"
        private const val eur = "eur"
    }

    interface CoinItemClickListener {
        fun onCoinClick(coinId: String)
    }
}