package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.databinding.FragmentTaxSearchOrdersBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.ui.adapter.TaxOrderAdapter
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaxSearchOrders: Fragment(R.layout.fragment_tax_search_orders) {
    private lateinit var binding: FragmentTaxSearchOrdersBinding
    private val viewModel by viewModel<TaxViewModel>()
    private var _adapter: TaxOrderAdapter? = null
    private val adapter get() = _adapter!!
    private val orders: ArrayList<Order> = arrayListOf()
    private val sellers = mutableMapOf<Int, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxSearchOrdersBinding.bind(view)

        initViews()
        initObservers()
        initListeners()
    }
    private fun initViews() {

        _adapter = TaxOrderAdapter()
        binding.rvSearchOrders.adapter = adapter
        binding.toggleByAutoNumber.isSelected = true
        binding.tilSearch.prefixText = getString(R.string.prefix_auto_number)
        binding.etSearch.requestFocus()
    }
    private fun initObservers() {

        lifecycleScope.launch {
            viewModel.getAllOrders()
            viewModel.getAllSellers()
        }

        viewModel.successFlow.onEach { orderResponse ->
            adapter.submitList(orderResponse.results.asReversed())
            orders.addAll(orderResponse.results)
        }.launchIn(lifecycleScope)

        viewModel.allSellersSuccessFlow.onEach {
            for (seller in it.results){
                sellers[seller.id] = seller.karer_name
            }
            adapter.sellers = sellers
        }.launchIn(lifecycleScope)

        viewModel.messageFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.errorFlow.onEach {
            makeToast("Error: ${it.message}")
        }.launchIn(lifecycleScope)

    }


    private fun initListeners() {
        binding.tbSearch.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        adapter.onOrderFineClickListener {
            makeToast("Fine clicked")
        }

        binding.groupFilters.addOnButtonCheckedListener { _, checkedId, _ ->
            when(checkedId){
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
                        it.car_number.lowercase().contains(query.toString())
                    }
                    binding.toggleByPassport.id -> {
                        it.driver_passport_or_id_number.lowercase().contains(query.toString())
                    }
                    binding.toggleByPhone.id -> {
                        it.driver_phone_number.lowercase().contains("+998${query.toString()}")
                    }
                    else  -> {it.car_number.contains(query.toString())}
                }
            }
            adapter.submitList(sortedList)
        }

    }

}