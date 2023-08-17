package com.theberdakh.carrierapp.ui.user.tax

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentTaxViolationsBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.ui.user.adapter.TaxOrderAdapter
import com.theberdakh.carrierapp.ui.user.adapter.TaxViolationAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxViolationsBinding.bind(view)

        initViews()
        initObservers()
        initListeners()


    }

    private fun initListeners() {

        adapter.onViolationClickListener {
            makeToast("Violation clicked")
        }

    }

    private fun initObservers() {

        lifecycleScope.launch {
            Log.d("Send", "get all orders request")
            viewModel.getAllViolations()
        }


        viewModel.violationSuccessFlow.onEach {
            Log.d("Order by Id Success", "Success ${it.results}")
            adapter.submitList(it.results)
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