package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.SellerSignUpBinding

class SellerSignUpFragment: Fragment(R.layout.seller_sign_up) {
    private lateinit var binding: SellerSignUpBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SellerSignUpBinding.bind(view)


    }

}