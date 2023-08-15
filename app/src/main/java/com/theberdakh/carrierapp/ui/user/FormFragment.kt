package com.theberdakh.carrierapp.ui.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentSellerFormBinding

class FormFragment: Fragment(R.layout.fragment_seller_form) {
    private lateinit var binding: FragmentSellerFormBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellerFormBinding.bind(view)



    }
}