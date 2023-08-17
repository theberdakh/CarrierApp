package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.TaxOfficerSignUpBinding
import com.theberdakh.carrierapp.ui.user.UserFragment

class TaxOfficerSignUpFragment: Fragment(R.layout.tax_officer_sign_up) {
    private lateinit var binding: TaxOfficerSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TaxOfficerSignUpBinding.bind(view)

        initViews()
        initListeners()


    }

    private fun initViews() {
        binding.switchId.isChecked = true
    }

    private fun initListeners() {
        binding.switchId.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.text = if (isChecked) "ID" else "Passport"
        }

        binding.btnRegister.setOnClickListener {
        }
    }


}