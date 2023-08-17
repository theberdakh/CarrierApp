package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.seller.SellerResponse
import com.theberdakh.carrierapp.databinding.SellerSignUpBinding
import com.theberdakh.carrierapp.presentation.RegisterViewModel
import com.theberdakh.carrierapp.util.makeToast
import com.theberdakh.carrierapp.util.showSnackBar
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks

class SellerSignUpFragment: Fragment(R.layout.seller_sign_up) {
    private lateinit var binding: SellerSignUpBinding
    private val viewModel by viewModel<RegisterViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SellerSignUpBinding.bind(view)

        initObservers()
        initListeners()
        initViews()

    }

    private fun initViews() {
    }

    private fun initObservers() {
        viewModel.successFlow.onEach {
            Log.d("Login Success", "Success ${it.token}")
            showSnackBar(binding.root, "Akkount registratciya etildi.")
            findNavController().popBackStack()
        }.launchIn(lifecycleScope)

        viewModel.messageFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.errorFlow.onEach {
            makeToast("Error")
        }.launchIn(lifecycleScope)

    }


    private fun initListeners() {
        binding.btnRegister.clicks().debounce(300).onEach {
            viewModel.registerSeller(binding.etName.text.toString(), "+${998}${binding.etPhoneNumber.text.toString()}", binding.etPassword.text.toString(), binding.etPassword.text.toString())
        }.launchIn(lifecycleScope)
    }

}