package com.theberdakh.carrierapp.ui.seller

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.databinding.FragmentOrderDetailsBinding
import java.util.Locale


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

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        Log.d("Image", order.car_photo)

        Glide.with(requireActivity())
            .load("http://86.107.197.112/${order.car_photo}")
            .placeholder(R.drawable.baseline_add_a_photo_24)
            .into(binding.ivOrder)



        val geocoder: Geocoder = Geocoder(requireContext(), Locale.getDefault())

       val  addresses = geocoder.getFromLocation(
            40.741895,
            -73.989308,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


        val address: String =
            addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val city: String = addresses[0].locality
        val state: String = addresses[0].adminArea
        val country: String = addresses[0].countryName
        val postalCode: String = addresses[0].postalCode
        val knownName: String = addresses[0].featureName

        val fullAdress = "$country, $state, $city"


        binding.apply {

            tvOrderType.text = if(order.cargo_type == 1) "Sheben" else "Topıraq (default)"
            tvOrderValue.text = if(order.weight.isNullOrEmpty()) "Null" else order.cargo_value
            tvOrderUnit.text = if(order.cargo_unit ==1 ) "m3" else "kg"
            tvCarrierName.text = if(order.driver_name.isNullOrEmpty()) "Null" else order.driver_name
            tvCarrierPassport.text = if(order.driver_passport_or_id_number.isNullOrEmpty()) "Null" else order.driver_passport_or_id_number.toString()
            tvCarrierPhone.text = if(order.driver_phone_number.isNullOrEmpty()) "Null" else order.driver_phone_number
            tvSellerName.text = if(order.karer.toString().isNullOrEmpty()) "Null" else order.karer.toString()
            tvAutoNumber.text = "Avto nomeri: ${order.car_number}"
            binding.tvOrderDate.text = "Sáne: ${order.date }"
            binding.tvOrderLocation.text = "Lokaciya: ${fullAdress}"
        }
    }


    override fun onDestroyView() {

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        super.onDestroyView()
    }

}