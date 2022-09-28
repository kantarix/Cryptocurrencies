package com.kantarix.cryptocurrencies.presentation.coin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.kantarix.cryptocurrencies.BackClickListener
import com.kantarix.cryptocurrencies.R
import kotlin.properties.Delegates

class CoinDetailsFragment : Fragment() {
    private var coinId by Delegates.notNull<String>()
    private var listener: BackClickListener? = null
    private var toolbar: androidx.appcompat.widget.Toolbar? = null

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

        initViews()
        setUpListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyViews()
    }

    private fun initViews() {
        toolbar = view?.findViewById(R.id.toolbar)
    }

    private fun setUpListeners() {
        toolbar?.setNavigationOnClickListener {
            listener?.onBackClick()
        }
    }

    private fun destroyViews() {
        toolbar = null
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