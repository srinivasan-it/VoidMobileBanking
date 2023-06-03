package com.example.voidmobilebanking.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.voidmobilebanking.BuildConfig
import com.example.voidmobilebanking.model.Currencies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpResponseException
import org.ksoap2.transport.HttpTransportSE
import java.net.SocketTimeoutException

/**
 * Make the network call to get currencies list from db using SOAP
 */
class DashboardViewModel : ViewModel() {

    private val soapObject = SoapObject(BuildConfig.NAME_SPACE, BuildConfig.METHOD_NAME)
    val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)

    private var _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var _currenciesList = MutableLiveData<ArrayList<Currencies>>()
    val currenciesList: LiveData<ArrayList<Currencies>> = _currenciesList

    fun getCurrencies() {
        envelope.dotNet = true
        envelope.setOutputSoapObject(soapObject)
        val tse = HttpTransportSE(BuildConfig.URL)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                try {
                    tse.call(BuildConfig.SOAP_ACTION, envelope)
                    withContext(Dispatchers.Main) {
                        parsePlainTextToModel(envelope.response.toString())
                    }
                } catch (e: HttpResponseException) {
                    withContext(Dispatchers.Main) {
                        _error.value = e.message.toString()
                    }
                }

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    _error.value = e.message.toString()
                }
            }
        }
    }

    /**
     * Didn't get the response as XML.
     * @param plainText is the response getting from API.
     * So using [split] to convert the response to model.
     */
    private fun parsePlainTextToModel(plainText: String) {

        val values = plainText.split("tCurrency=anyType")

        val splittedByIsoCode = arrayListOf<String>()
        values.forEach {
            splittedByIsoCode.addAll(it.split("sISOCode="))
        }

        val splittedByName = arrayListOf<String>()
        splittedByIsoCode.forEach {
            splittedByName.addAll(it.split("; sName="))
        }

        val semicolon = arrayListOf<String>()
        splittedByName.forEach {
            semicolon.addAll(it.split("; };"))
        }

        val redundant = arrayListOf<String>()
        semicolon.forEach {
            if (notContainsFillers(it))
                redundant.add(it)
        }

        val code = arrayListOf<String>()
        val name = arrayListOf<String>()

        redundant.forEachIndexed { index, s ->
            if (index == 0) {
                code.add(s)
            } else if (index == 1) {
                name.add(s)
            } else if (index % 2 == 1) {
                name.add(s)
            } else {
                code.add(s)
            }
        }

        val currencies = arrayListOf<Currencies>()

        code.forEachIndexed { index, s ->
            currencies.add(Currencies(currencyCode = code[index], currencyName = name[index]))
        }

        _currenciesList.value = currencies

    }

    private fun notContainsFillers(it: String) =
        it != "anyType{" && it != "{" && it != "}" && it != " }" && it != "" && it != " "


    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return DashboardViewModel() as T
            }
        }
    }
}