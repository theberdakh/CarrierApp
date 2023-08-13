package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentSignUpBinding
import com.theberdakh.carrierapp.ui.authentication.adapter.SignUpViewPagerAdapter

class SignUpFragment: Fragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)

        setListeners()
        setViews()

    }

    private fun setViews() {
      binding.vpSignUp.adapter = SignUpViewPagerAdapter(arrayListOf(SellerSignUpFragment(), TaxOfficerSignUpFragment()), requireActivity().supportFragmentManager, requireActivity().lifecycle)

      TabLayoutMediator(binding.tblSignUp, binding.vpSignUp){ tab, position ->
          when(position){
              0 -> tab.text = "Satıwshı"
              1 -> tab.text = "Salıq xizmetshisi"
          }

      }.attach()
    }

    private fun setListeners() {
        binding.tbSignUp.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }


}