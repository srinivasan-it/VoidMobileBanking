package com.example.voidmobilebanking.view

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.voidmobilebanking.R
import com.example.voidmobilebanking.databinding.FragmentAuthBinding

/**
 * [AuthFragment] - used to log in the app
 */
class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding

    private val codeIN = "IN"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater)
        binding.lifecycleOwner = this
        setActionListener()
        return binding.root
    }

    private fun setActionListener() {
        binding.etMobileNumber.addTextChangedListener(
            PhoneNumberFormattingTextWatcher(
                codeIN
            )
        )
        binding.etMobileNumber.addTextChangedListener {
            binding.tilMobileNumber.error = null
        }

        binding.pvOtp.setOnFocusChangeListener { _, _ ->
            phoneNumberValidation()
        }

        binding.btnLogin.setOnClickListener {
            if (phoneNumberValidation())
                otpValidation()
        }
    }

    private fun phoneNumberValidation(): Boolean {
        return if (binding.etMobileNumber.text.toString().isEmpty()) {
            binding.tilMobileNumber.error = getString(R.string.enter_phone_number)
            false
        } else if (binding.etMobileNumber.text.toString().length < 10) {
            binding.tilMobileNumber.error = getString(R.string.enter_valid_phone_number)
            false
        } else {
            true
        }
    }

    private fun toast(s: String) {
        closeSoftKeyboard()
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    private fun otpValidation() {
        val ph = binding.etMobileNumber.text.toString()
        val otp = ph.substring(6, 10)
        val enteredOtp = binding.pvOtp.text.toString()
        if (enteredOtp.isEmpty()) {
            toast(getString(R.string.enter_otp))
        } else if (enteredOtp.length < 4 || enteredOtp != otp) {
            toast(getString(R.string.invalid_otp))
        } else {
            findNavController().navigate(R.id.action_authFragment_to_dashboardFragment)
        }
    }

    private fun closeSoftKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}