package com.kantarix.cryptocurrencies.presentation.coinslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kantarix.cryptocurrencies.R

class CoinsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_coins_list, container, false)
    }

    companion object {
        fun newInstance(param1: String, param2: String) = CoinsListFragment()
    }
}