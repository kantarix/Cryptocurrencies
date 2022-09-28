package com.kantarix.cryptocurrencies.presentation.coin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toolbar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.kantarix.cryptocurrencies.BackClickListener
import com.kantarix.cryptocurrencies.CoinsRepositoryProvider
import com.kantarix.cryptocurrencies.R
import com.kantarix.cryptocurrencies.model.Coin
import com.kantarix.cryptocurrencies.model.CoinDetails
import com.kantarix.cryptocurrencies.presentation.ViewState
import com.kantarix.cryptocurrencies.presentation.coinslist.CoinsViewModel
import kotlin.properties.Delegates

class CoinDetailsFragment : Fragment() {
    private lateinit var viewModel: CoinsViewModel
    private var coinId by Delegates.notNull<String>()
    private var listener: BackClickListener? = null
    private var toolbar: androidx.appcompat.widget.Toolbar? = null
    private var detailsView: ConstraintLayout? = null
    private var errorView: LinearLayout? = null
    private var errorBtn: AppCompatButton? = null
    private var loadingView: ProgressBar? = null
    private var coinImage: AppCompatImageView? = null
    private var coinDescription: AppCompatTextView? = null
    private var coinCategories: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { coinId = it.getString(PARAM_COIN).toString() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BackClickListener)
            listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_coin_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = CoinsViewModel((requireActivity() as CoinsRepositoryProvider).provideCoinsRepository())

        initViews()
        setUpListeners()

        viewModel.coin.observe(this.viewLifecycleOwner, this::updateCoinDetails)
        viewModel.state.observe(this.viewLifecycleOwner, this::updateState)

        viewModel.loadCoin(coinId)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyViews()
    }

    private fun updateCoinDetails(coin: CoinDetails) {
        coinImage?.let {
            Glide.with(requireActivity())
                .load(coin.image.large)
                .into(it)
        }
        toolbar?.title = coin.name
        coinDescription?.text = coin.description.en
        coinCategories?.text = coin.categories.joinToString(separator = ", ")
    }

    private fun updateState(state: ViewState) {
        loadingView?.visibility = View.GONE
        detailsView?.visibility = View.GONE
        errorView?.visibility = View.GONE
        when (state) {
            is ViewState.Error -> {
                errorView?.visibility = View.VISIBLE
            }
            is ViewState.Loading -> {
                loadingView?.visibility = View.VISIBLE
            }
            is ViewState.Result -> {
                detailsView?.visibility = View.VISIBLE
            }
        }
    }

    private fun initViews() {
        toolbar = view?.findViewById(R.id.toolbar)
        detailsView = view?.findViewById(R.id.details_view)
        errorView = view?.findViewById(R.id.error_view)
        errorBtn = view?.findViewById(R.id.error_btn)
        loadingView = view?.findViewById(R.id.loading_view)
        coinImage = view?.findViewById(R.id.coin_image)
        coinDescription = view?.findViewById(R.id.description_text)
        coinCategories = view?.findViewById(R.id.categories_text)
    }

    private fun setUpListeners() {
        toolbar?.setNavigationOnClickListener {
            listener?.onBackClick()
        }
    }

    private fun destroyViews() {
        toolbar = null
        detailsView = null
        errorView = null
        errorBtn = null
        loadingView = null
        coinImage = null
        coinDescription = null
        coinCategories = null
    }

    companion object {
        private const val PARAM_COIN = "COIN_ID"

        fun newInstance(coinId: String): CoinDetailsFragment {
            val fragment = CoinDetailsFragment()
            val args = Bundle()
            args.putString(PARAM_COIN, coinId)
            fragment.arguments = args
            return fragment
        }
    }

}