package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.databinding.FragmentTaxBinding
import com.theberdakh.carrierapp.ui.authentication.adapter.SignUpViewPagerAdapter
import com.theberdakh.carrierapp.ui.seller.SellerOrders
import com.theberdakh.carrierapp.ui.seller.SellerViolations

class TaxFragment : Fragment(R.layout.fragment_tax){
    private lateinit var binding: FragmentTaxBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxBinding.bind(view)
        initViews()
        initListeners()
    }
    private fun initListeners() {
        binding.tbTax.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.tbTax.setOnMenuItemClickListener { menu ->
            when(menu.itemId){
                R.id.action_logout -> {
                    SharedPrefStorage().signedIn= false
                    requireActivity().finish()
                    startActivity(requireActivity().intent)
                    requireActivity().overridePendingTransition(0,0)
                    true
                }
                R.id.action_profile -> {
                    findNavController().navigate(TaxFragmentDirections.actionTaxFragmentToTaxProfile())
                    true
                }

                R.id.action_settings ->{
                    findNavController().navigate(TaxFragmentDirections.actionTaxFragmentToTaxSettings())
                    true
                }
                else -> {true}
            }
        }
    }

    private fun initViews() {
        binding.vpTax.adapter = SignUpViewPagerAdapter(arrayListOf(TaxViolationsFragment(), TaxOrdersFragment()), requireActivity().supportFragmentManager, requireActivity().lifecycle)
        binding.tbTax.title = SharedPrefStorage().name

        TabLayoutMediator(binding.tblTax, binding.vpTax){ tab, position ->
            when(position){
                0 -> tab.text = "Járiymalar"
                1 -> tab.text = "Buyırtpalar"
            }
        }.attach()

    }
}