package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentTaxOrdersBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.ui.adapter.TaxOrderAdapter
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaxOrdersFragment: Fragment(R.layout.fragment_tax_orders) {
    private lateinit var binding: FragmentTaxOrdersBinding
    private val viewModel by viewModel<TaxViewModel>()
    private var _adapter: TaxOrderAdapter? = null
    private val adapter get() = _adapter!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxOrdersBinding.bind(view)


        initViews()
        initObservers()
        initListeners()


    }

    private fun initViews() {
        _adapter = TaxOrderAdapter()
        binding.rvTaxOrders.adapter = adapter

    }

    private fun initObservers() {

        lifecycleScope.launch {
            Log.d("Send", "get all orders request")
            viewModel.getAllOrders()
        }


        viewModel.successFlow.onEach {
            Log.d("Order by Id Success", "Success ${it.results}")
            adapter.submitList(it.results.asReversed())
        }.launchIn(lifecycleScope)

        viewModel.messageFlow.onEach {
            Log.d("Order by Id Message", "mess ${it}")

            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.errorFlow.onEach {
            Log.d("Order by Id error", "errir")

            makeToast("Error, check your Internet connection")
        }.launchIn(lifecycleScope)
    }


    private fun initListeners() {

        adapter.onOrderClickListener {
            findNavController().navigate(TaxFragmentDirections.actionTaxFragmentToTaxFormFragment())
        }

        adapter.onOrderFineClickListener {
            makeToast("Fine clicked")
        }

        binding.fabSearchOrders.setOnClickListener {
            findNavController().navigate(TaxFragmentDirections.actionTaxFragmentToTaxSearchOrders())
        }


    }
}