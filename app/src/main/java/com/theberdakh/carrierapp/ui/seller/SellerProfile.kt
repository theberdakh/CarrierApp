package com.theberdakh.carrierapp.ui.seller

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.databinding.FragmentSellerProfileBinding

class SellerProfile: Fragment(R.layout.fragment_seller_profile) {

    private var _binding: FragmentSellerProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSellerProfileBinding.bind(view)


        initViews()
        initListeners()


    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tbProfile.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViews() {

        binding.apply {
            etName.setText(SharedPrefStorage().name)
            etPhoneNumber.setText((SharedPrefStorage().phoneNumber))
            etPassword.setText(SharedPrefStorage().password)
        }

    }

    private fun initObservers() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}