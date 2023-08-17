package com.theberdakh.carrierapp.ui.user.tax

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentTaxViolationsBinding

class TaxViolationsFragment: Fragment(R.layout.fragment_tax_violations)
{
    private lateinit var binding: FragmentTaxViolationsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxViolationsBinding.bind(view)


    }
}