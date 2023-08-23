package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.response.violation.Violation
import com.theberdakh.carrierapp.databinding.FragmentUpdateViolationBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaxUpdateViolation: Fragment(R.layout.fragment_update_violation) {
    private lateinit var binding: FragmentUpdateViolationBinding
    private val viewModel by viewModel<TaxViewModel>()
    private val args: TaxUpdateViolationArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpdateViolationBinding.bind(view)




    }

    private fun initObservers(id: Int) {
        lifecycleScope.launch {
            viewModel.getViolationById(id)
        }

        viewModel.singleViolationSuccessFlow.onEach {
            setViolation(it)


        }.launchIn(lifecycleScope)

    }

    private fun setViolation(violation: Violation) {

        binding.apply {
            Glide.with(requireActivity())
                .load(violation.car_photo)
                .placeholder(R.drawable.baseline_add_a_photo_24)
                .thumbnail(Glide.with(requireActivity()).load(violation.car_photo))
                .into(binding.ivFormImage)


            etAutoNumber.setText(violation.car_number)
            etCarrierAutoBrand.setText(violation.car_brand)
            etCarrierName.setText(violation.driver_name)
            etCarrierPhone.setText(violation.driver_phone_number)
            atvSellerName.setText(violation.karer_name)
            val doc = if (violation.driver_passport_or_id == "id") "ID" else "Passport"
            atvDocumentType.setText(doc)
            etPassportSeries.setText(violation.driver_passport_or_id_number)
            atvCargoType.setText(violation.cargo_type)


        }

    }
}