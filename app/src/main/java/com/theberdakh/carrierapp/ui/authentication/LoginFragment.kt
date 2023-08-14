package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.LoginResponse
import com.theberdakh.carrierapp.databinding.FragmentLoginBinding
import com.theberdakh.carrierapp.presentation.LoginViewModel
import com.theberdakh.carrierapp.util.checkText
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks
import kotlin.math.log

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModel<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        initObservers()
        initViews()
        initListeners()
    }

    private fun initObservers() {
        viewModel.successFlow.onEach {
            Log.d("Login Success", "Success ${it.access}")
            saveResponse(it)
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToUserFragment())

        }.launchIn(lifecycleScope)

        viewModel.messageFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.errorFlow.onEach {
            makeToast("Error")
        }.launchIn(lifecycleScope)
    }

    private fun saveResponse(loginResponse: LoginResponse) {
        SharedPrefStorage().phoneNumber = binding.etUsername.text.toString()
        SharedPrefStorage().accessToken = loginResponse.access
        SharedPrefStorage().refreshToken = loginResponse.refresh
    }

    private fun initViews() {
        binding.tvNewAccount.linksClickable = false

        if (binding.etUsername.text.toString()
                .isNotEmpty() && binding.btnLogin.checkText(binding.etPassword, 8)
        ) {
            binding.btnLogin.isEnabled = true
        }
    }

    private fun initListeners() {

        binding.btnLogin.clicks().debounce(200).onEach {
            Log.d("Login", "Clicked")
            viewModel.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
            Log.d("Login click", "${binding.etUsername.text.toString()} ,${binding.etPassword.text.toString()} ")
        }.launchIn(lifecycleScope)


    }
}