package com.example.voidmobilebanking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.voidmobilebanking.adapter.binder.CurrenciesBinder
import com.example.voidmobilebanking.databinding.ListItemCurrencyBinding
import com.example.voidmobilebanking.model.Currencies

/**
 * [RecyclerView.Adapter] to list the currencies data in the UI
 */
class CurrenciesAdapter(
    private val viewLifeCycleOwner: LifecycleOwner
) : RecyclerView.Adapter<CurrenciesAdapter.CurrenciesViewHolder>() {

    private var currencies = emptyList<Currencies>()

    /**
     * [RecyclerView.ViewHolder] to hold the currency data in the UI
     */
    inner class CurrenciesViewHolder(
        private val itemBinding: ListItemCurrencyBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        private val binder = CurrenciesBinder()

        fun bind(currencies: Currencies) {
            binder.bind(currencies)
            itemBinding.binder = binder
            itemBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCurrencyBinding.inflate(inflater, parent, false)
        binding.lifecycleOwner = viewLifeCycleOwner
        return CurrenciesViewHolder(binding)
    }

    override fun getItemCount() = currencies.size

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        holder.bind(currencies[position])
    }

    fun submitData(currencies: List<Currencies>) {
        this.currencies = currencies
        notifyItemRangeChanged(0, currencies.size)
    }
}