package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentPhoneRegisterBinding

class PhoneRegisterFragment: Fragment(R.layout.fragment_phone_register) {
    private lateinit var binding: FragmentPhoneRegisterBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPhoneRegisterBinding.bind(view)
    }
}