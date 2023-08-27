package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.databinding.FragmentTaxProfileBinding
import com.theberdakh.carrierapp.util.setCustomAdapter

class TaxProfile: Fragment(R.layout.fragment_tax_profile) {
    private var _binding: FragmentTaxProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTaxProfileBinding.bind(view)

        initViews()
        initListeners()
    }

    private fun initListeners() {
        binding.btnUpdate.setOnClickListener { findNavController().popBackStack() }
        binding.tbProfile.setNavigationOnClickListener { findNavController().popBackStack() }
    }

    private fun initViews() {
        binding.apply {

            etFirstLastPoetricName.setText(SharedPrefStorage().name)
            etPhoneNumber.setText((SharedPrefStorage().phoneNumber))
            etPassportSeries.setText(SharedPrefStorage().passportOrIdNumber)
            etPassword.setText(SharedPrefStorage().password)

            atvPosition.setText(SharedPrefStorage().position)
            atvPosition.setCustomAdapter(listOf("Basliq", "Orinbasar"))

            atvRegion.setText(SharedPrefStorage().region)
            atvRegion.setCustomAdapter(listOf("Qaraqalpaqstan Respublikasi", "Andijan", "Buxara", "Ferg'ana", "Jizzax", "Xorezm", "Namangan", "Nawayi", "Qashqadarya", "Samarqand", "Sirdarya", "Surxandarya", "Tashkent"))

            atvDocumentType.setText("${if (SharedPrefStorage().passportOrId == "passport") "Passport" else "ID"}")
            atvDocumentType.setCustomAdapter(listOf("Passport", "ID"))
        }
    }

    private fun initObservers() {

    //Update logic
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}