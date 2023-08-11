package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentPhoneConfirmationBinding

class PhoneConfirmationFragment(): Fragment(R.layout.fragment_phone_confirmation) {
private lateinit var binding: FragmentPhoneConfirmationBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhoneConfirmationBinding.bind(view)
    }
}