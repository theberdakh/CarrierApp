package com.theberdakh.carrierapp.ui.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.databinding.FragmentOrderDetailsBinding

class OrderDetailsFragment: Fragment(R.layout.fragment_order_details) {
    private lateinit var binding: FragmentOrderDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderDetailsBinding.bind(view)
        val args: OrderDetailsFragmentArgs by navArgs()

        initViews(args.order)
        initListeners()



    }

    private fun initListeners() {

        binding.tbOrder.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun initViews(order: Order) {
        binding.apply {

            Glide.with(requireActivity())
                .load(order.car_photo)
                .placeholder(R.drawable.baseline_add_a_photo_24)
                .into(binding.ivOrder)



            tvOrderType.text = if(order.cargo_type.toString().isNullOrEmpty()) "Null" else order.cargo_type.toString()
            tvOrderValue.text = if(order.cargo_value.isNullOrEmpty()) "Null" else order.cargo_value
            tvOrderUnit.text = if(order.cargo_unit.toString().isNullOrEmpty()) "Null" else order.cargo_unit.toString()
            tvCarrierName.text = if(order.driver_name.isNullOrEmpty()) "Null" else order.driver_name
            tvCarrierPassport.text = if(order.driver_passport_or_id_number.isNullOrEmpty()) "Null" else order.driver_passport_or_id_number.toString()
            tvCarrierPhone.text = if(order.driver_phone_number.isNullOrEmpty()) "Null" else order.driver_phone_number
            tvSellerName.text = if(order.karer.toString().isNullOrEmpty()) "Null" else order.karer.toString()
        }

    }

}