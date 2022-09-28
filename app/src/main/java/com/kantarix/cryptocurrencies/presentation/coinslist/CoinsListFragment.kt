package com.kantarix.cryptocurrencies.presentation.coinslist

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.kantarix.cryptocurrencies.CoinsRepositoryProvider
import com.kantarix.cryptocurrencies.R
import com.kantarix.cryptocurrencies.data.CoinsRepository
import com.kantarix.cryptocurrencies.data.remote.CoinApiService
import com.kantarix.cryptocurrencies.data.remote.NetworkModule
import com.kantarix.cryptocurrencies.data.remote.NoConnectionInterceptor
import com.kantarix.cryptocurrencies.model.Coin
import com.kantarix.cryptocurrencies.presentation.ViewState

class CoinsListFragment : Fragment() {

    private lateinit var viewModel: CoinsViewModel
    private lateinit var coinsAdapter: CoinsAdapter
    private var rvCoins: RecyclerView? = null
    private var listener: CoinItemClickListener? = null
    private var errorView: LinearLayout? = null
    private var errorBtn: AppCompatButton? = null
    private var loadingView: ProgressBar? = null

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

        viewModel = CoinsViewModel((requireActivity() as CoinsRepositoryProvider).provideCoinsRepository())

        initViews()
        setUpListeners()
        setUpCoinsAdapter()

        viewModel.coinsList.observe(this.viewLifecycleOwner, this::updateList)
        viewModel.state.observe(this.viewLifecycleOwner, this::updateState)

        if (savedInstanceState == null) viewModel.loadCoins()

    }

    override fun onDestroy() {
        super.onDestroy()
        destroyViews()
    }

    private fun updateCurrency(currency: String) {
        viewModel.currentCurrency = currency
        viewModel.loadCoins()
    }

    private fun updateList(coinsList: List<Coin>) {
        coinsAdapter.currency = viewModel.currentCurrency
        coinsAdapter.submitList(coinsList)
    }

    private fun updateState(state: ViewState) {
        loadingView?.visibility = View.GONE
        rvCoins?.visibility = View.GONE
        errorView?.visibility = View.GONE
        when (state) {
            is ViewState.Error -> {
                errorView?.visibility = View.VISIBLE
            }
            is ViewState.Loading -> {
                loadingView?.visibility = View.VISIBLE
            }
            is ViewState.Result -> {
                rvCoins?.visibility = View.VISIBLE
            }
        }
    }

    private fun initViews() {
        chipUsd = view?.findViewById(R.id.chip_usd)
        chipEur = view?.findViewById(R.id.chip_eur)
        rvCoins = view?.findViewById(R.id.coins_list_rv)
        errorView = view?.findViewById(R.id.error_view)
        errorBtn = view?.findViewById(R.id.error_btn)
        loadingView= view?.findViewById(R.id.loading_view)
    }

    private fun setUpListeners() {
        chipUsd?.setOnClickListener {
            val currency = chipUsd?.text ?: CoinsViewModel.USD
            updateCurrency(currency.toString())
        }
        chipEur?.setOnClickListener {
            val currency = chipEur?.text ?: CoinsViewModel.USD
            updateCurrency(currency.toString())
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
        errorView = null
        errorBtn = null
        loadingView = null
    }

    companion object {
        fun newInstance() = CoinsListFragment()
    }

    interface CoinItemClickListener {
        fun onCoinClick(coinId: String)
    }
}