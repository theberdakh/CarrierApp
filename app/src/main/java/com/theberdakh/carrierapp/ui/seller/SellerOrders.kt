package com.theberdakh.carrierapp.ui.seller

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
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
        }.launchIn(lifecycleScope)

    }

    private fun initListeners() {


        adapter.onOrderClickListener {
            findNavController().navigate(
                SellerFragmentDirections.actionUserFragmentToOrderDetailsFragment(
                    it.id
                )
            )
        }

        binding.fabAddOrders.setOnClickListener {
            findNavController().navigate(
                SellerFragmentDirections.actionUserFragmentToFormFragment()
            )
        }


    }
}