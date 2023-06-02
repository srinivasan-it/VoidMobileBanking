package com.example.voidmobilebanking.adapter.binder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.voidmobilebanking.model.Currencies

/**
 * Created by srinivasan on 02/06/23.
 */
class CurrenciesBinder {

    private var data = MutableLiveData<Currencies>()

    var name: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(data) {
            value = it.currencyName
        }
    }

    var code: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(data) {
            value = it.currencyCode
        }
    }

    fun bind(currencies: Currencies) {
        data.value = currencies
    }

}