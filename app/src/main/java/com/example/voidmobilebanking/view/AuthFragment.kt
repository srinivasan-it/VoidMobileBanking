package com.example.voidmobilebanking.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.voidmobilebanking.R
import com.example.voidmobilebanking.databinding.FragmentAuthBinding

/**
 * [AuthFragment] - used to log in the app
 */
class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }


}