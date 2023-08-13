package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentPhoneRegisterBinding
import com.theberdakh.carrierapp.util.checkText

class PhoneRegisterFragment: Fragment(R.layout.fragment_phone_register) {
    private lateinit var binding: FragmentPhoneRegisterBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhoneRegisterBinding.bind(view)

        initViews()
        initListeners()

    }

    private fun initViews() {
        binding.btnSms.checkText(binding.etPhone, 9)
    }

    private fun initListeners() {
        binding.btnSms.setOnClickListener {
            findNavController().navigate(PhoneRegisterFragmentDirections.actionPhoneRegisterFragmentToPhoneConfirmationFragment())
        }
    }

}