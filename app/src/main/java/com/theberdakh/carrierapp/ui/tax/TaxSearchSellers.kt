package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.data.model.response.seller.GetAllSellerResult
import com.theberdakh.carrierapp.databinding.FragmentTaxSearchSellersBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.ui.adapter.TaxOrderAdapter
import com.theberdakh.carrierapp.ui.adapter.TaxSellersAdapter
import com.theberdakh.carrierapp.util.makeToast
import com.theberdakh.carrierapp.util.setCustomAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaxSearchSellers: Fragment(R.layout.fragment_tax_search_sellers) {
    private lateinit var binding: FragmentTaxSearchSellersBinding
    private val viewModel by viewModel<TaxViewModel>()
    private var _adapter: TaxSellersAdapter? = null
    private val adapter get() = _adapter!!
    private val sellers: ArrayList<GetAllSellerResult> = arrayListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxSearchSellersBinding.bind(view)


        initViews()
        initObservers()
        initListeners()


    }

    private fun initListeners() {

        binding.tbSearch.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        adapter.onSellerClickListener {
            SharedPrefStorage().lastSellerId = it.id
            SharedPrefStorage().lastSellerName = it.karer_name
            SharedPrefStorage().lastSelectedPhone = it.phone_number
            findNavController().popBackStack()
        }


        binding.etSearch.addTextChangedListener {query ->
            val sortedList = sellers.filter {
                when(binding.groupFilters.checkedButtonId){
                    binding.toggleByName.id -> {
                        it.karer_name.lowercase().contains(query.toString().lowercase())
                    }
                    binding.toggleByPhone.id -> {
                        it.phone_number.contains("+998$query")
                    }
                    else  -> {it.karer_name.lowercase().contains(query.toString().lowercase())}
                }
            }
            adapter.submitList(sortedList)
        }


        binding.groupFilters.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when(checkedId){
                binding.toggleByName.id -> {
                    Log.d("Toggle", "Selected by name")
                    binding.tilSearch.prefixText = "Ati:"
                }
                binding.toggleByPhone.id -> {
                    Log.d("Toggle", "Selected phone")
                    binding.tilSearch.prefixText = "Tel:+998"
                }
            }
        }
    }

    private fun initViews() {
        _adapter = TaxSellersAdapter()
        binding.rvSearchOrders.adapter = adapter
        binding.toggleByName.isSelected = true
        binding.tilSearch.prefixText = "Ati:"
        binding.etSearch.requestFocus()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.getAllSellers()
        }


        viewModel.allSellersSuccessFlow.onEach {
            adapter.submitList(it.results)
            sellers.addAll(it.results)
        }.launchIn(lifecycleScope)

        viewModel.allSellersMessageFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.allSellersErrorFlow.onEach {
            makeToast(it.message.toString())
        }.launchIn(lifecycleScope)
    }

}