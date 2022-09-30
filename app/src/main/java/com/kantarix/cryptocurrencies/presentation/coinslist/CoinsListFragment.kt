package com.kantarix.cryptocurrencies.presentation.coinslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.kantarix.cryptocurrencies.CoinsRepositoryProvider
import com.kantarix.cryptocurrencies.R
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
    private var swipeRefresh: SwipeRefreshLayout? = null
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

        viewModel.loadCoins()
    }

    override fun onDestroy() {
        destroyViews()
        super.onDestroy()
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
        when (state) {
            is ViewState.Error -> showErrorView()
            is ViewState.Loading -> showLoadingView()
            is ViewState.Result -> showResultView()
        }
    }

    private fun showLoadingView() {
        if (swipeRefresh?.isRefreshing == true) {
            rvCoins?.isVisible = true
        } else {
            errorView?.isVisible = false
            rvCoins?.isVisible = false
            loadingView?.isVisible = true
        }
    }

    private fun showResultView() {
        if (swipeRefresh?.isRefreshing == true) {
            errorView?.isVisible = false
            swipeRefresh?.isRefreshing = false
        } else {
            loadingView?.isVisible = false
        }
        rvCoins?.isVisible = true
    }

    private fun showErrorView() {
        if (swipeRefresh?.isRefreshing == true) {
            swipeRefresh?.isRefreshing = false
            showErrorSnackbar()
        } else {
            loadingView?.isVisible = false
            rvCoins?.isVisible = false
            errorView?.isVisible = true
        }
    }

    private fun showErrorSnackbar() {
        swipeRefresh?.let {
            Snackbar.make(it, getString(R.string.error_pull_refresh), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.burnt_sienna))
                .show()
        }
    }

    private fun initViews() {
        chipUsd = view?.findViewById(R.id.chip_usd)
        chipEur = view?.findViewById(R.id.chip_eur)
        rvCoins = view?.findViewById(R.id.coins_list_rv)
        errorView = view?.findViewById(R.id.error_view)
        errorBtn = view?.findViewById(R.id.error_btn)
        loadingView = view?.findViewById(R.id.loading_view)
        swipeRefresh = view?.findViewById(R.id.swipe_refresh)
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
        errorBtn?.setOnClickListener {
            viewModel.loadCoins()
        }
        swipeRefresh?.setOnRefreshListener {
            viewModel.loadCoins()
        }
    }

    private fun setUpCoinsAdapter() {
        coinsAdapter = CoinsAdapter(viewModel.currentCurrency) { coin ->
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
        swipeRefresh = null
    }

    companion object {
        fun newInstance() = CoinsListFragment()
    }

    interface CoinItemClickListener {
        fun onCoinClick(coinId: String)
    }
}