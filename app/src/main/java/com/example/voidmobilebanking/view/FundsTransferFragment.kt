package com.example.voidmobilebanking.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.voidmobilebanking.R
import com.example.voidmobilebanking.databinding.FragmentFundsTransferBinding
import com.example.voidmobilebanking.databinding.LayoutSuccessDialogBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Screen which helps to transfer amount to the account
 */
class FundsTransferFragment : Fragment() {

    private lateinit var binding: FragmentFundsTransferBinding

    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFundsTransferBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionListener()
    }

    private fun setActionListener() {
        binding.etAmount.setOnFocusChangeListener { _, _ ->
            phoneNumberValidation()
        }
        binding.btnTransfer.setOnClickListener {
            if (phoneNumberValidation())
                amountValidation()
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

    private fun amountValidation() {
        val amt = binding.etAmount.text.toString()
        if (amt.isEmpty() || amt.toInt() < 0) {
            binding.tilAmount.error = getString(R.string.enter_valid_amount)
        } else if (amt.toInt() > 0) {
            showSuccessDialog()
        }
    }

    private fun showSuccessDialog() {
        dialog = AlertDialog.Builder(requireContext()).create()
        val dBinding = LayoutSuccessDialogBinding.inflate(layoutInflater)
        dialog?.let {
            it.setView(dBinding.root)
            it.setCancelable(false)
            it.show()
        }
        lifecycleScope.launch {
            delay(5000)
            dialog?.dismiss()
            dialog = null
            findNavController().navigateUp()
        }
    }


}