package com.example.voidmobilebanking.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.voidmobilebanking.constant.Constants
import com.example.voidmobilebanking.adapter.CurrenciesAdapter
import com.example.voidmobilebanking.databinding.FragmentAllCurrenciesBinding
import com.example.voidmobilebanking.model.CurrenciesList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * create an instance of this fragment.
 */
class AllCurrenciesFragment : Fragment() {

    private lateinit var binding: FragmentAllCurrenciesBinding

    private val adapter by lazy {
        CurrenciesAdapter(this)
    }

    private var currenciesList: CurrenciesList? = CurrenciesList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllCurrenciesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        getParcel()
        binding.rvCurrencyList.adapter = adapter
        shimmer(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(1500)
            shimmer(false)
            currenciesList?.let {
                it.currenciesList?.let { currencies ->
                    adapter.submitData(currencies)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getParcel() {
        currenciesList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arguments?.getParcelable(Constants.CURRENCIES_LIST, CurrenciesList::class.java)
        else
            arguments?.getParcelable(Constants.CURRENCIES_LIST)
    }

    private fun shimmer(visible: Boolean) {
        if (visible) {
            binding.shimmer.startShimmer()
            binding.shimmer.visibility = View.VISIBLE
            binding.currencies.visibility = View.GONE
        } else {
            binding.shimmer.stopShimmer()
            binding.shimmer.visibility = View.GONE
            binding.currencies.visibility = View.VISIBLE
        }
    }


}