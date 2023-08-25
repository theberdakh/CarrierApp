package com.theberdakh.carrierapp.ui.tax

import android.os.Bundle
import android.util.Log
import android.view.View
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
            viewModel.getAllSellers()
        }

        viewModel.allSellersSuccessFlow.onEach {

            Log.d("TaxOrdersFragment", "${it.results}")

            for (seller in it.results) {
                sellers[seller.id] = seller.karer_name
            }
            Log.d("Tax Orders", "${sellers.size}")
            adapter.sellers = sellers
        }.launchIn(lifecycleScope)

        viewModel.allSellersMessageFlow.onEach {
            Log.d("All Sellers", it)
        }.launchIn(lifecycleScope)

        viewModel.allSellersErrorFlow.onEach {
            Log.d("All Sellers", it.stackTraceToString())
        }.launchIn(lifecycleScope)

        viewModel.successFlow.onEach {
            Log.d("Order by Id Success", "Success ${it.results}")
            adapter.submitList(it.results.asReversed())
            orders.addAll(it.results.asReversed())

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


    private fun String.extractString(from: Int, until: Int) = this.substring(from, until)

    private fun initListeners() {

      binding.swipeRefreshTaxOrders.setOnRefreshListener {
          initObservers()
          binding.swipeRefreshTaxOrders.isRefreshing = false
      }


        adapter.onOrderClickListener {
            findNavController().navigate(
                TaxFragmentDirections.actionTaxFragmentToOrderDetailsFragment(
                    it.id,
                    true
                )
            )
        }


        binding.fabSearchOrders.setOnClickListener {
            findNavController().navigate(TaxFragmentDirections.actionTaxFragmentToTaxSearchOrders())
        }

        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->

           lifecycleScope.launch {
            viewModel.getAllOrders()
           }

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            Log.d("Date", sdf.format(Date()))

            val sortedList = orders.filter {
                when (checkedId) {
                    binding.btnByDay.id -> {
                        it.date == "1"//sdf.format(Date())
                    }
                    binding.btnByWeek.id -> {
                        it.date.endsWith("08")
                    }
                    else -> {
                        true
                    }
                }
            }
            adapter.submitList(sortedList)
        }


    }
}