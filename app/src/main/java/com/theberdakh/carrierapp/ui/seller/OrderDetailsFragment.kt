package com.theberdakh.carrierapp.ui.seller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentOrderDetailsBinding
import com.theberdakh.carrierapp.presentation.SellerViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class OrderDetailsFragment: Fragment(R.layout.fragment_order_details) {
    private  var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SellerViewModel>()
    private val args: OrderDetailsFragmentArgs by navArgs()
    private var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers(args.id)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOrderDetailsBinding.bind(view)

        initViews()
        initListeners()

    }

    private fun initObservers(id: Int) {
        lifecycleScope.launch {
            viewModel.getOrderById(id)
        }

        viewModel.orderSuccessFlow.onEach {

            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

           Glide.with(requireActivity())
                .load(it.car_photo)
                .placeholder(R.drawable.baseline_add_a_photo_24)
                .thumbnail(Glide.with(requireActivity()).load(it.car_photo))
                .into(binding.ivViolation)

            this.id = it.id


            binding.apply {
                tvOrderType.text = it.cargo_type
                tvOrderValue.text = "${it.cargo_value} ${if(it.cargo_unit ==1 ) "m3" else "kg"}"
                tvCarrierName.text = it.driver_name
                tvCarrierPassport.text = it.driver_passport_or_id_number
                tvCarrierPhone.text = it.driver_phone_number
                tvSellerName.text = it.karer.toString()
                tvAutoNumber.text = it.car_number
                binding.tvOrderDate.text = it.date
                binding.tvOrderLocation.text = "Lokatciya ${it.location}"


                binding.tvTrailerWeight.text = if (it.trailer == "Bar") "${it.trailer_weight} Tonna" else "Joq"

            }

            binding.tvOrderLocation.setOnClickListener {view->
                val gmmIntentUri = Uri.parse("google.streetview:cbll=${it.location}");
                val mapIntent =  Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent)
            }


        }.launchIn(lifecycleScope)
    }

    private fun initListeners() {



        binding.tbOrder.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAddViolation.setOnClickListener {
            findNavController().navigate(OrderDetailsFragmentDirections.actionOrderDetailsFragmentToTaxFormFragment(id))
        }

    }

    private fun initViews() {

        binding.btnAddViolation.isVisible = args.isTaxOfficer

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


    }

    override fun onResume() {
        super.onResume()
        initObservers(args.id)
    }


    override fun onDestroyView() {

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        super.onDestroyView()
    }

}