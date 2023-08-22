package com.theberdakh.carrierapp.ui.seller

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.databinding.FragmentSellerBinding
import com.theberdakh.carrierapp.ui.authentication.adapter.SignUpViewPagerAdapter
import com.theberdakh.carrierapp.ui.tax.TaxOrdersFragment
import com.theberdakh.carrierapp.ui.tax.TaxViolationsFragment
import com.theberdakh.carrierapp.util.makeToast

class SellerFragment: Fragment(R.layout.fragment_seller) {
    private lateinit var binding: FragmentSellerBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellerBinding.bind(view)

        initViews()
        initObservers()
        initListeners()



    }

    private fun initViews() {
        binding.vpSeller.adapter = SignUpViewPagerAdapter(arrayListOf(SellerOrders(), SellerViolations()), requireActivity().supportFragmentManager, requireActivity().lifecycle)

        binding.tbSeller.title = SharedPrefStorage().name


        TabLayoutMediator(binding.tblSeller, binding.vpSeller){ tab, position ->
            when(position){
                0 -> tab.text = "Buyırtpalar"
                1 -> tab.text = "Járiymalar"
            }
        }.attach()

    }

    private fun initObservers() {

    }

    private fun initListeners() {


        binding.tbSeller.setOnMenuItemClickListener { menu ->
            when(menu.itemId){
                R.id.action_logout -> {
                    SharedPrefStorage().signedIn= false
                    requireActivity().finish()
                    startActivity(requireActivity().intent)
                    requireActivity().overridePendingTransition(0,0)
                    true
                }

                R.id.action_profile_tax -> {
                    makeToast("Clicked")
                    findNavController().navigate(SellerFragmentDirections.actionUserFragmentToSellerProfile())
                    true
                }

                R.id.action_settings -> {
                    true
                }
                else -> {true}
            }
        }



    }
}