package com.example.voidmobilebanking.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
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
 * Created by srinivasan on 02/06/23.
 */
class DashboardViewModel : ViewModel() {

    // ViewModelProvider(this, MainViewModel.Factory)[MainViewModel::class.java]

    val soapObject = SoapObject(BuildConfig.NAME_SPACE, BuildConfig.METHOD_NAME)
    val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)

    init {

        envelope.dotNet = true
        envelope.setOutputSoapObject(soapObject)

        val tse = HttpTransportSE(BuildConfig.URL)

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    try {
                        tse.call(BuildConfig.SOAP_ACTION, envelope)


                        val resultString = envelope.response
                        var currencies = parsePlainTextToModel(envelope.response.toString())
                        Log.e("", "parsePlainTextToModel is " + currencies)
                    } catch (e: HttpResponseException) {
                        Log.e("", "error is " + e.message.toString())
                    }

                }
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
            }
        }
    }

    fun parsePlainTextToModel(plainText: String): Currencies? {
        // Split the plain text into individual values
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

        val zzz = arrayListOf<String>()
        semicolon.forEach {
            if (it != "anyType{" && it != "{" && it != "}" && it != " }" && it != "" && it != " ") {
                zzz.add(it)
            }
        }

        val code = arrayListOf<String>()
        val name = arrayListOf<String>()

        zzz.forEachIndexed { index, s ->
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

        Log.e("", "codes are: " + code)
        Log.e("", "names are: " + name)

        val currencies = arrayListOf<Currencies>()


        code.forEachIndexed { index, s ->
            currencies.add(Currencies(currencyCode = code[index], currencyName = name[index]))
        }
        Log.e("", "namessss are: " + currencies)


        try {

            val code = zzz[0]
            val currencyName = zzz[1]

            return Currencies(code, currencyName)
        } catch (e: Exception) {
            return null
        }
    }


    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return DashboardViewModel() as T
            }
        }
    }
}