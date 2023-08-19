package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.databinding.FragmentTaxSearchBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.ui.adapter.TaxOrderAdapter
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaxSearchOrders: Fragment(R.layout.fragment_tax_search) {
    private lateinit var binding: FragmentTaxSearchBinding
    private val viewModel by viewModel<TaxViewModel>()
    private var _adapter: TaxOrderAdapter? = null
    private val adapter get() = _adapter!!
    private val orders: ArrayList<Order> = arrayListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxSearchBinding.bind(view)

        initViews()
        initObservers()
        initListeners()


    }

    private fun initViews() {

        _adapter = TaxOrderAdapter()
        binding.rvSearchOrders.adapter = adapter
    }

    private fun initObservers() {

        lifecycleScope.launch {
            Log.d("Send", "get all orders request")
            viewModel.getAllOrders()
        }


        viewModel.successFlow.onEach { orderResponse ->
            Log.d("Order by Id Success", "Success ${orderResponse.results}")

            adapter.submitList(orderResponse.results.asReversed())
            orders.addAll(orderResponse.results)

            if (orderResponse.results.isEmpty()){
                binding.searchProgress.isVisible = false
            }

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
        binding.tbSearch.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        adapter.onOrderFineClickListener {
            makeToast("Fine clicked")
        }

        binding.etSearch.addTextChangedListener {query ->
            val sortedList = orders.filter {
                it.car_number.startsWith(query.toString())
            }

            if (sortedList.isEmpty()){
                binding.searchProgress.isVisible = false
            }


            adapter.submitList(sortedList)
        }

    }
}