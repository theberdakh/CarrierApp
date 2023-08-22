package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentTaxSettingsBinding

class TaxSettings: Fragment(R.layout.fragment_tax_settings) {
    private var _binding: FragmentTaxSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaxSettingsBinding.bind(view)

        initViews()
        initListeners()
    }

    private fun initListeners() {
        binding.tbTaxSettings.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViews() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}