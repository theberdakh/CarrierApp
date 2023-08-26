package com.theberdakh.carrierapp.ui.tax

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.data.model.response.violation.Violation
import com.theberdakh.carrierapp.databinding.FragmentTaxOrdersBinding
import com.theberdakh.carrierapp.presentation.TaxViewModel
import com.theberdakh.carrierapp.ui.adapter.TaxOrderAdapter
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaxOrdersFragment : Fragment(R.layout.fragment_tax_orders) {
    private lateinit var binding: FragmentTaxOrdersBinding
    private val viewModel by viewModel<TaxViewModel>()
    private var _adapter: TaxOrderAdapter? = null
    private val orders: ArrayList<Order> = arrayListOf()

    private val adapter get() = _adapter!!
    private var sellers = mutableMapOf<Int, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObservers()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaxOrdersBinding.bind(view)


        initViews()
        initListeners()


    }

    private fun initViews() {
        _adapter = TaxOrderAdapter()
        binding.rvTaxOrders.adapter = adapter

    }

    private fun initObservers() {

        lifecycleScope.launch {
            viewModel.getAllOrders()
            viewModel.getAllSellers()
        }

        viewModel.allSellersSuccessFlow.onEach {
            for (seller in it.results) {
                sellers[seller.id] = seller.karer_name
            }
            adapter.sellers = sellers
        }.launchIn(lifecycleScope)

        viewModel.allSellersMessageFlow.onEach {
        }.launchIn(lifecycleScope)

        viewModel.allSellersErrorFlow.onEach {
        }.launchIn(lifecycleScope)

        viewModel.successFlow.onEach {
            adapter.submitList(it.asReversed())
            orders.addAll(it.asReversed())

        }.launchIn(lifecycleScope)

        viewModel.messageFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.errorFlow.onEach {
            makeToast("Error, check your Internet connection")
        }.launchIn(lifecycleScope)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {

        binding.btnAllOrders.setOnClickListener {
            adapter.submitList(null)
            lifecycleScope.launch {
                viewModel.getAllOrders()
                viewModel.getAllSellers()
            }
        }

        binding.btnByDay.setOnClickListener {
            adapter.submitList(null)
            lifecycleScope.launch {
                viewModel.getOrdersByDay()
                viewModel.getAllSellers()
            }
        }


        adapter.onOrderClickListener {
            findNavController().navigate(
                TaxFragmentDirections.actionTaxFragmentToOrderDetailsFragment(
                    it.id,
                    true
                )
            )
        }

        binding.swipeRefreshTaxOrders.setOnRefreshListener {
            adapter.submitList(null)

            when (binding.toggleButton.checkedButtonId){
                binding.btnAllOrders.id -> {
                    lifecycleScope.launch {
                        viewModel.getAllOrders()
                        viewModel.getAllSellers()
                    }
                }
                binding.btnByDay.id -> {
                    lifecycleScope.launch {
                        viewModel.getOrdersByDay()
                        viewModel.getAllSellers()
                    }
                }
            }

            binding.swipeRefreshTaxOrders.isRefreshing = false
        }


        binding.fabSearchOrders.setOnClickListener {
            findNavController().navigate(TaxFragmentDirections.actionTaxFragmentToTaxSearchOrders())
        }

       /* binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            Log.d("Date", sdf.format(Date()))

            val sortedList = orders.filter {
                when (checkedId) {
                    binding.btnByDay.id -> {
                        it.date == "1"//sdf.format(Date())
                    }
                    else -> {
                        true
                    }
                }
            }
            adapter.submitList(sortedList)
        }*/


    }
}