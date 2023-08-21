package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.button.MaterialButtonToggleGroup.OnButtonCheckedListener
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

    private companion object {
        const val SORT_AUTO_NUMBER = 0
        const val SORT_PASSPORT = 1
        const val SORT_PHONE = 2
    }
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
        binding.toggleByAutoNumber.isSelected = true
        binding.tilSearch.prefixText = "Avtomobil nomeri:"
        binding.etSearch.requestFocus()
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

        binding.groupFilters.addOnButtonCheckedListener { group, checkedId, isChecked ->

            when(binding.groupFilters.checkedButtonId){
                binding.toggleByAutoNumber.id -> {
                    binding.tilSearch.hint = "Misali: 95A123CD"
                    binding.tilSearch.prefixText = "Avtomobil nomeri:"
                }
                binding.toggleByPassport.id -> {
                    binding.tilSearch.hint = "KA12345"
                    binding.tilSearch.prefixText = "Passport:"
                }
                binding.toggleByPhone.id -> {
                    binding.tilSearch.hint = "991234567"
                    binding.tilSearch.prefixText = "Tel: +998"
                }
            }



        }


        binding.etSearch.addTextChangedListener {query ->
            val sortedList = orders.filter {

                when(binding.groupFilters.checkedButtonId){
                    binding.toggleByAutoNumber.id -> {
                        it.car_number.contains(query.toString())

                    }
                    binding.toggleByPassport.id -> {
                        it.driver_passport_or_id_number.contains(query.toString())
                    }
                    binding.toggleByPhone.id -> {
                        it.driver_phone_number.contains("+998${query.toString()}")
                    }
                    else  -> {it.car_number.contains(query.toString())}
                }
            }

            if (sortedList.isEmpty()){
                binding.searchProgress.isVisible = false
            }

            adapter.submitList(sortedList)
        }

    }

}