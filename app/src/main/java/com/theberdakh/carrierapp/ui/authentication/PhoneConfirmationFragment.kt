package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.databinding.FragmentPhoneConfirmationBinding
import com.theberdakh.carrierapp.util.checkText

class PhoneConfirmationFragment() : Fragment(R.layout.fragment_phone_confirmation) {
    private lateinit var binding: FragmentPhoneConfirmationBinding
    private var phoneNumber: String = ""
    private val args: PhoneConfirmationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneNumber = args.phoneNumber
        binding = FragmentPhoneConfirmationBinding.bind(view)

        initListeners()
        initViews()

    }

    private fun initViews() {
        binding.tvHelper.text = "$phoneNumber ǵa 5 xanalı kod jiberildi."
        binding.btnConfirmation.checkText(binding.etCode, 5)
    }

    private fun initListeners() {

        binding.tbConfirmation.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnConfirmation.setOnClickListener {
            findNavController().navigate(PhoneConfirmationFragmentDirections.actionPhoneConfirmationFragmentToLoginFragment())
            SharedPrefStorage().phoneNumber = phoneNumber
        }
    }
}