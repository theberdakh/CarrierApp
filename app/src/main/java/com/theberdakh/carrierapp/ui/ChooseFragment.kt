package com.theberdakh.carrierapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentChooseBinding

class ChooseFragment: Fragment(R.layout.fragment_choose) {
    private lateinit var binding: FragmentChooseBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChooseBinding.bind(view)



    }
}