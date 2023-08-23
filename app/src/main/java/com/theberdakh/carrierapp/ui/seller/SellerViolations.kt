package com.theberdakh.carrierapp.ui.seller

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.response.violation.Violation
import com.theberdakh.carrierapp.databinding.FragmentSellerBinding
import com.theberdakh.carrierapp.databinding.FragmentSellerViolationsBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.ui.adapter.TaxViolationAdapter
import com.theberdakh.carrierapp.ui.tax.TaxFragmentDirections
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerViolations : Fragment(
    R.layout.fragment_seller_violations
) {
    private lateinit var binding: FragmentSellerViolationsBinding
    private val viewModel by viewModel<TaxViewModel>()
    private var _adapter: TaxViolationAdapter? = null
    private val adapter get() = _adapter!!
    private val violations: ArrayList<Violation> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellerViolationsBinding.bind(view)

        initViews()
        initObservers()
        initListeners()

    }

    private fun initListeners() {

        adapter.onViolationClickListener {
            makeToast("Violation clicked")
        }

        binding.fabAddNewViolation.setOnClickListener {
            findNavController().navigate(TaxFragmentDirections.actionTaxFragmentToTaxFormFragment(-1))
        }

        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val sortedList = violations.filter {
                when(binding.toggleButton.checkedButtonId){
                    binding.btnEnteredIncorrect.id -> {
                        it.reason_violation =="entered_incorrect"
                    }
                    binding.btnNotEntered.id -> {
                        it.reason_violation == "not_entered"
                    }
                    else -> it.reason_violation== "entered_incorrect"}
            }
            adapter.submitList(sortedList)
        }

    }

    private fun initObservers() {

        lifecycleScope.launch {
            Log.d("Send", "get all orders request")
            viewModel.getAllViolations()
        }


        viewModel.violationSuccessFlow.onEach {
            Log.d("Order by Id Success", "Success ${it.results}")
            adapter.submitList(violations)
            violations.addAll(
                it.results
            )
        }.launchIn(lifecycleScope)

        viewModel.violationMessageFlow.onEach {
            Log.d("Order by Id Message", "mess ${it}")

            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.violationErrorFlow.onEach {
            Log.d("Order by Id error", "errir")
            makeToast("Error, check your Internet connection")
        }.launchIn(lifecycleScope)

    }

    private fun initViews() {
        _adapter = TaxViolationAdapter()
        binding.rvTaxViolations.adapter = adapter
    }
}