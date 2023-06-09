package com.example.voidmobilebanking.view

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.voidmobilebanking.R
import com.example.voidmobilebanking.constant.Constants
import com.example.voidmobilebanking.databinding.FragmentAuthBinding
import com.example.voidmobilebanking.utils.Utils

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
        binding.lifecycleOwner = this
        setActionListener()
        return binding.root
    }

    private fun setActionListener() {
        binding.etMobileNumber.addTextChangedListener(
            PhoneNumberFormattingTextWatcher(
                Constants.CODE_INDIA
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

    /**
     * Last four digits of the entered mobile number will be the OTP to login
     */
    private fun otpValidation() {
        val ph = binding.etMobileNumber.text.toString()
        val otp = ph.substring(6, 10)
        val enteredOtp = binding.pvOtp.text.toString()
        if (enteredOtp.isEmpty()) {
            Utils.showToast(requireContext(), requireView(), getString(R.string.enter_otp))
        } else if (enteredOtp.length < 4 || enteredOtp != otp) {
            Utils.showToast(requireContext(), requireView(), getString(R.string.invalid_otp))
        } else {
            findNavController().navigate(R.id.action_authFragment_to_dashboardFragment)
        }
    }

}