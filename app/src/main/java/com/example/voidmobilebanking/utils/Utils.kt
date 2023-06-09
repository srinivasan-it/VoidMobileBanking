package com.example.voidmobilebanking.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Created by srinivasan on 09/06/23.
 */
object Utils {

    fun showToast(context: Context, view: View, msg: String) {
        closeSoftKeyboard(context, view)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun closeSoftKeyboard(context: Context, view: View) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}