package com.theberdakh.carrierapp.ui.user

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.databinding.FragmentUserBinding
import com.theberdakh.carrierapp.presentation.SellerViewModel
import com.theberdakh.carrierapp.ui.user.adapter.OrderAdapter
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment: Fragment(R.layout.fragment_user) {
    private lateinit var binding: FragmentUserBinding
    private val viewModel by viewModel<SellerViewModel>()
    private var _adapter: OrderAdapter? = null
    private val adapter get() = _adapter!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserBinding.bind(view)

        initViews()
        initObservers()
        initListeners()





    }

    private fun initViews() {

        binding.tbUser.title = SharedPrefStorage().name
        binding.tbUser.subtitle = SharedPrefStorage().type


        _adapter = OrderAdapter()
        binding.rvUser.adapter = adapter
        lifecycleScope.launch {
            Log.d("Send", "Send Token")
            viewModel.getOrdersById( 10)
        }


    }

    private fun initObservers() {
        viewModel.postOrderByIdSuccessFlow.onEach {
            Log.d("Order by Id Success", "Success ${it.size}")

          /*  val mutableListOrders = mutableListOf<Result>()
            for (order in it.results){
                if (order.karer == SharedPrefStorage().id){
                    mutableListOrders.add(order)
                }
            }*/

            adapter.submitList(it)
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

        binding.fbUser.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToFormFragment())
        }

        adapter.onOrderClickListener {
            findNavController().navigate(UserFragmentDirections.actionUserFragmentToOrderDetailsFragment(it))
        }


    }
}