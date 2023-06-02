package com.example.voidmobilebanking.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.voidmobilebanking.R

/**
 * A simple [Fragment] subclass.
 * Use the [FundsTransferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FundsTransferFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_funds_transfer, container, false)
    }


}