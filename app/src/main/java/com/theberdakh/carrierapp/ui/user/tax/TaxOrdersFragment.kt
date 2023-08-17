package com.theberdakh.carrierapp.ui.user.tax

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentTaxOrdersBinding

class TaxOrdersFragment: Fragment(R.layout.fragment_tax_orders) {
    private lateinit var binding: FragmentTaxOrdersBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxOrdersBinding.bind(view)


    }
}