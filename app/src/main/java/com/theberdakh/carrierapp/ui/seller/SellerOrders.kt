package com.theberdakh.carrierapp.ui.seller

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
import com.theberdakh.carrierapp.databinding.FragmentSellerOrdersBinding
import com.theberdakh.carrierapp.presentation.SellerViewModel
import com.theberdakh.carrierapp.ui.adapter.OrderAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerOrders : Fragment(R.layout.fragment_seller_orders) {
    private lateinit var binding: FragmentSellerOrdersBinding
    private val viewModel by viewModel<SellerViewModel>()
    private var _adapter: OrderAdapter? = null
    private val adapter get() = _adapter!!
    private val orders: ArrayList<Order> = arrayListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSellerOrdersBinding.bind(view)

        initViews()
        initObservers()
        initListeners()

    }


    private fun initViews() {


        _adapter = OrderAdapter()
        binding.rvOrders.adapter = adapter
        lifecycleScope.launch {
            Log.d("Send", "Send Token")
            viewModel.getOrdersBySellerId(SharedPrefStorage().id)
        }


    }

    private fun initObservers() {

        viewModel.postOrderByIdSuccessFlow.onEach {
            Log.d("Order by Id Success", "Success ${it.size}")

            adapter.submitList(it.asReversed())
            orders.addAll(it)
        }.launchIn(lifecycleScope)

    }

    private fun initListeners() {

        binding.swipeRefreshSellerOrders.setOnRefreshListener {
            initObservers()
            binding.swipeRefreshSellerOrders.isRefreshing = false
        }


        adapter.onOrderClickListener {
            findNavController().navigate(
                SellerFragmentDirections.actionUserFragmentToOrderDetailsFragment(
                    it.id, false
                )
            )
        }

        binding.fabAddOrders.setOnClickListener {
            findNavController().navigate(
                SellerFragmentDirections.actionUserFragmentToFormFragment()
            )
        }

        binding.tilSearch.hint = "Buyirtpalardan izlew..."

     /*   binding.toggleButton.addOnButtonCheckedListener { _, checkedId, _ ->
            when (checkedId) {
                binding.btnSortByName.id -> {
                    binding.tilSearch.hint = "Ati:"
                    binding.tilSearch.prefixText = "Ati:"
                }

                binding.btnSortByPhone.id -> {
                    binding.tilSearch.hint = "Tel: +998"
                    binding.tilSearch.prefixText = "Tel: +998:"
                }

                binding.btnSortByCar.id -> {
                    binding.tilSearch.hint = "Avtomobil nomeri:"
                    binding.tilSearch.prefixText = "Avtomobil nomeri:"
                }
            }
        }*/

        binding.etSearch.addTextChangedListener { query ->
            val sortedList = orders.filter {
                when (binding.toggleButton.checkedButtonId) {
                    binding.btnSortByName.id -> {
                        it.driver_name.lowercase().contains(query.toString())
                    }

                    binding.btnSortByPhone.id -> {
                        it.driver_phone_number.lowercase().contains(query.toString())
                    }

                    binding.btnSortByCar.id -> {
                        it.car_number.lowercase().contains(query.toString())
                    }

                    else -> {
                        it.driver_name.contains(query.toString())
                    }
                }
            }
            adapter.submitList(sortedList)
        }


    }
}