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

class TaxViolationsFragment : Fragment(R.layout.fragment_tax_violations) {
    private lateinit var binding: FragmentTaxViolationsBinding
    private val viewModel by viewModel<TaxViewModel>()
    private var _adapter: TaxViolationAdapter? = null
    private val adapter get() = _adapter!!

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
            viewModel.getAllViolations()
        }

        viewModel.violationSuccessFlow.onEach {
          adapter.submitList(it)
        }.launchIn(lifecycleScope)

        viewModel.violationMessageFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.violationErrorFlow.onEach {
            makeToast("Error, check your Internet connection")
        }.launchIn(lifecycleScope)

    }

    private fun initListeners() {

        adapter.onViolationClickListener {
            findNavController().navigate(
                TaxFragmentDirections.actionTaxFragmentToTaxCheckViolation(
                    it.id,
                    true
                )
            )
        }



        binding.swipeRefresh.setOnRefreshListener {
            adapter.submitList(null)

            when (binding.toggleButton.checkedButtonId){
                binding.btnAll.id -> {
                    lifecycleScope.launch {
                        viewModel.getAllViolations()
                    }
                }
                binding.btnNotEntered.id -> {
                    lifecycleScope.launch{
                        viewModel.getNotEnteredViolations()
                    }
                }
                binding.btnEnteredIncorrect.id -> {
                    lifecycleScope.launch{
                        viewModel.getEnteredIncorrectViolations()
                    }
                }
            }
            binding.swipeRefresh.isRefreshing = false
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
            } else {
                findNavController().navigate(
                    TaxFragmentDirections.actionTaxFragmentToTaxFormFragment(
                        -1
                    )
                )
            }
        }

        binding.btnAll.setOnClickListener{
            adapter.submitList(null)
            lifecycleScope.launch {
                viewModel.getAllViolations()
            }
        }

        binding.btnNotEntered.setOnClickListener{
            adapter.submitList(null)
            lifecycleScope.launch {
                viewModel.getNotEnteredViolations()
            }
        }

        binding.btnEnteredIncorrect.setOnClickListener {
            adapter.submitList(null)
            lifecycleScope.launch {
                viewModel.getEnteredIncorrectViolations()
            }
        }

    }


}