package com.theberdakh.carrierapp.ui.seller

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.databinding.FragmentOrderDetailsBinding
import com.theberdakh.carrierapp.presentation.SellerViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class OrderDetailsFragment: Fragment(R.layout.fragment_order_details) {
    private lateinit var binding: FragmentOrderDetailsBinding
    private val viewModel by viewModel<SellerViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderDetailsBinding.bind(view)
        val args: OrderDetailsFragmentArgs by navArgs()

        initObservers(args.id)
        initViews()
        initListeners()

    }

    private fun initObservers(id: Int) {
        lifecycleScope.launch {
            viewModel.getOrdersById(id)
        }

        viewModel.orderSuccessFlow.onEach {

            Glide.with(requireActivity())
                .load(it.car_photo)
                .placeholder(R.drawable.baseline_add_a_photo_24)
                .into(binding.ivOrder)


            binding.apply {
                tvOrderType.text = it.cargo_type.toString()
                tvOrderValue.text = it.cargo_value
                tvOrderUnit.text = if(it.cargo_unit ==1 ) "m3" else "kg"
                tvCarrierName.text = it.driver_name
                tvCarrierPassport.text = it.driver_passport_or_id_number
                tvCarrierPhone.text = it.driver_phone_number
                tvSellerName.text = it.karer.toString()
                tvAutoNumber.text = it.car_number
                binding.tvOrderDate.text = it.date
                binding.tvOrderLocation.text = it.location
            }
        }.launchIn(lifecycleScope)
    }

    private fun initListeners() {

        binding.tbOrder.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun initViews() {

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


    }


    override fun onDestroyView() {

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        super.onDestroyView()
    }

}