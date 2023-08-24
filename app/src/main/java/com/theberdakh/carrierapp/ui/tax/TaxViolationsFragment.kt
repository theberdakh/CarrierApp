package com.theberdakh.carrierapp.ui.tax

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.data.model.response.violation.Violation
import com.theberdakh.carrierapp.databinding.FragmentTaxViolationsBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.ui.adapter.TaxViolationAdapter
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaxViolationsFragment: Fragment(R.layout.fragment_tax_violations)
{
    private lateinit var binding: FragmentTaxViolationsBinding
    private val viewModel by viewModel<TaxViewModel>()
    private var _adapter: TaxViolationAdapter? = null
    private val adapter get() = _adapter!!
    private val violations: ArrayList<Violation> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxViolationsBinding.bind(view)

        initViews()
        initObservers()
        initListeners()


    }

    private fun initViews() {
        _adapter = TaxViolationAdapter()
        binding.rvTaxViolations.adapter = adapter
    }

    private fun initObservers() {

        lifecycleScope.launch {
            Log.d("Send", "get all violations request")
            viewModel.getAllViolations()
        }


        viewModel.violationSuccessFlow.onEach {
            Log.d("Order by Id Success", "Success ${it.results}")
            adapter.submitList(it.results.asReversed())
            violations.addAll(it.results)
        }.launchIn(lifecycleScope)

        viewModel.violationMessageFlow.onEach {
            Log.d("Order by Id Message", "mess ${it}")

            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.violationErrorFlow.onEach {
            Log.d("Order by Id error", "${it.printStackTrace()}")

            makeToast("Error, check your Internet connection")
        }.launchIn(lifecycleScope)

    }

    private fun initListeners() {

        adapter.onViolationClickListener {
           findNavController().navigate(TaxFragmentDirections.actionTaxFragmentToTaxCheckViolation(it.id))
        }

        binding.fabAddNewViolation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100
                )
            }
            else {
                findNavController().navigate(TaxFragmentDirections.actionTaxFragmentToTaxFormFragment(-1))
            }
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
                   else ->true
                }
                }
            adapter.submitList(sortedList)
        }

    }







}