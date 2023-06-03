package com.example.voidmobilebanking.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class CurrenciesList(
    val currenciesList: @RawValue List<Currencies>? = null
) : Parcelable


data class Currencies(
    val currencyCode: String,
    val currencyName: String
)
