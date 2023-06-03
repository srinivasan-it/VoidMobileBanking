package com.example.voidmobilebanking.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.voidmobilebanking.Constants
import com.example.voidmobilebanking.R
import com.example.voidmobilebanking.adapter.CurrenciesAdapter
import com.example.voidmobilebanking.databinding.FragmentDashboardBinding
import com.example.voidmobilebanking.model.Currencies
import com.example.voidmobilebanking.model.CurrenciesList
import com.example.voidmobilebanking.viewmodel.DashboardViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.stream.Collectors

/**
 * [DashboardFragment] Dashboard for the app
 *
 */
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    private val amount = "₹ 69999.89"

    private val viewModel by lazy {
        ViewModelProvider(this, DashboardViewModel.Factory)[DashboardViewModel::class.java]
    }

    private val adapter by lazy {
        CurrenciesAdapter(this)
    }

    private var currenciesList = arrayListOf<Currencies>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.rvCurrencyList.adapter = adapter
        binding.rvCurrencyList.itemAnimator = null
        shimmer(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionListener()
        observe()
    }

    private fun observe() {
        viewModel.currenciesList.observe(viewLifecycleOwner) {
            currenciesList = it
            shimmer(false)
            val first20Elements = it.stream().limit(20).collect(Collectors.toList())
            adapter.submitData(first20Elements)
        }
    }

    private fun setActionListener() {
        binding.tvBalance.setOnClickListener {
            manageMask()
        }

        binding.tvViewAll.setOnClickListener {
            findNavController().navigate(
                R.id.action_dashboardFragment_to_allCurrenciesFragment,
                Bundle().apply {
                    putParcelable(Constants.CURRENCIES_LIST, CurrenciesList(currenciesList))
                })
        }

        binding.fabTransfer.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_fundsTransferFragment)
        }
    }

    private fun manageMask() {
        binding.tvBalance.text = amount
        lifecycleScope.launch {
            delay(3000)
            binding.tvBalance.text = getString(R.string.view_balance)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCurrencies()
    }

    private fun shimmer(visible: Boolean) {
        if (visible) {
            binding.shimmer.startShimmer()
            binding.shimmer.visibility = View.VISIBLE
            binding.dashboard.visibility = View.GONE
        } else {
            binding.shimmer.stopShimmer()
            binding.shimmer.visibility = View.GONE
            binding.dashboard.visibility = View.VISIBLE
        }
    }

}