package com.theberdakh.carrierapp.ui.seller

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
import com.theberdakh.carrierapp.ui.adapter.OrderAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerFragment: Fragment(R.layout.fragment_user) {
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

        binding.tbUser.title = "Buyırtpalar"
        binding.tbUser.subtitle = SharedPrefStorage().name


        _adapter = OrderAdapter()
        binding.rvUser.adapter = adapter
        lifecycleScope.launch {
            Log.d("Send", "Send Token")
            viewModel.getOrdersById( SharedPrefStorage().id)
        }


    }

    private fun initObservers() {
        viewModel.postOrderByIdSuccessFlow.onEach {
            Log.d("Order by Id Success", "Success ${it.size}")

            adapter.submitList(it.asReversed())
        }.launchIn(lifecycleScope)

    }

    private fun initListeners() {

        binding.fbUser.setOnClickListener {
            findNavController().navigate(SellerFragmentDirections.actionUserFragmentToFormFragment())
        }

        adapter.onOrderClickListener {
            findNavController().navigate(SellerFragmentDirections.actionUserFragmentToOrderDetailsFragment(
                    it
                )
            )
        }

        binding.tbUser.setOnMenuItemClickListener { menu ->
            when(menu.itemId){
                R.id.action_logout -> {
                    SharedPrefStorage().signedIn= false

                    requireActivity().finish()
                    startActivity(requireActivity().intent)
                    requireActivity().overridePendingTransition(0,0)
                    true
                }
                else -> {true}
            }
        }


    }
}