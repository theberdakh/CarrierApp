package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.fake.Updates
import com.theberdakh.carrierapp.data.model.response.violation.Violation
import com.theberdakh.carrierapp.databinding.FragmentCheckViolationBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.ui.adapter.ViolationChangesAdapter
import com.theberdakh.carrierapp.util.checkLocationPermissions
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaxCheckViolation: Fragment(R.layout.fragment_check_violation) {
    private lateinit var binding: FragmentCheckViolationBinding
    private val viewModel by viewModel<TaxViewModel>()
    private val args: TaxCheckViolationArgs by navArgs()
    private var _adapter: ViolationChangesAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCheckViolationBinding.bind(view)
        initViews()
       initListeners()
    }

    private fun initViews() {
        binding.tbCheckViolation.menu.findItem(R.id.action_edit_violation).isVisible = args.isTaxOfficer
        _adapter = ViolationChangesAdapter()
        binding.rvCheckViolation.adapter = adapter

    }

    private fun initListeners() {
        binding.tbCheckViolation.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.tbCheckViolation.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_edit_violation -> {
                   if( requireActivity().checkLocationPermissions()){
                       findNavController().navigate(TaxCheckViolationDirections.actionTaxCheckViolationToTaxUpdateViolation(args.id))
                   }
                    else {
                        makeToast("O'zgeris kiritiwin'iz ushin lokaciyan'zidi aniqlawg'a ruxsat beriwin'iz kerek.")
                   }
                   true
                }

                else -> {true}
            }
        }
    }
    private fun initObservers(id: Int) {
        lifecycleScope.launch { viewModel.getViolationById(id) }
        viewModel.singleViolationSuccessFlow.onEach { setViolation(it)
            setChanges(it.unique_number) }.launchIn(lifecycleScope)

        viewModel.uniqueViolationSuccessFlow.onEach {
            adapter.submitList(it)
        }.launchIn(lifecycleScope)
    }

    private fun setChanges(uniqueNumber: Int) {
        lifecycleScope.launch { viewModel.getViolationByUnique(uniqueNumber) }
    }


    private fun setViolation(violation: Violation) {


        binding.apply {
            Glide.with(requireActivity())
                .load(violation.car_photo)
                .placeholder(R.drawable.baseline_add_a_photo_24)
                .thumbnail(Glide.with(requireActivity()).load(violation.car_photo))
                .into(binding.ivFormImage)

            tvCargoDate.text = violation.cargo_date
            tvReasonViolation.text = violation.reason_violation
            tvOrderLocation.text = violation.location
            tvCarNumber.text = violation.car_number
            tvCarModel.text = violation.car_brand
            tvDriverName.text = violation.driver_name
            tvDriverPhone.text = violation.driver_phone_number
            tvSellerName.text = violation.karer_name
            tvDriverPassportNumber.text = violation.driver_passport_or_id_number
            tvCargoType.text = violation.cargo_type
        }
    }

    override fun onResume() {
        super.onResume()
        initObservers(args.id)
    }
}