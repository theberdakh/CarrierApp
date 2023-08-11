package com.theberdakh.carrierapp.ui.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentCameraBinding

class CameraFragment: Fragment(R.layout.fragment_camera) {
    private lateinit var binding: FragmentCameraBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)

    }
}