package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.login.LoginResponse
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


        if (SharedPrefStorage().signedIn){
            if (SharedPrefStorage().type == "tax_officer"){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTaxFragment())
            } else {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToUserFragment())
            }
        }

        initObservers()
        initViews()
        initListeners()
    }

    private fun initObservers() {

        viewModel.successFlow.onEach {
        Log.d("Login Success", "Success ${it.token}")

            if (it.type == "tax_officer"){
                saveResponseTax(it)
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTaxFragment())
            } else {
                saveResponseKarer(it)
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToUserFragment())
            }


    }.launchIn(lifecycleScope)

        viewModel.messageFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.errorFlow.onEach {
            makeToast("Error")
        }.launchIn(lifecycleScope)

    }

    private fun saveResponseKarer(loginResponse: LoginResponse) {
        SharedPrefStorage().id = loginResponse.id
        SharedPrefStorage().type = loginResponse.type
        SharedPrefStorage().phoneNumber = binding.etUsername.text.toString()
        SharedPrefStorage().token = loginResponse.token
        SharedPrefStorage().name = if(loginResponse.karer_name.isNullOrEmpty()) "Null" else loginResponse.karer_name
        SharedPrefStorage().signedIn = true
    }

    private fun saveResponseTax(loginResponse: LoginResponse) {
        SharedPrefStorage().id = loginResponse.id
        SharedPrefStorage().type = loginResponse.type
        SharedPrefStorage().phoneNumber = binding.etUsername.text.toString()
        SharedPrefStorage().token = loginResponse.token
        SharedPrefStorage().name = if(loginResponse.karer_name.isNullOrEmpty()) "Null" else loginResponse.karer_name


        SharedPrefStorage().id = loginResponse.id
        SharedPrefStorage().type = loginResponse.type
        SharedPrefStorage().phoneNumber = binding.etUsername.text.toString()
        SharedPrefStorage().token = loginResponse.token
        SharedPrefStorage().password = binding.etPassword.text.toString()
       SharedPrefStorage().signedIn = true
        SharedPrefStorage().name = loginResponse.full_name
        SharedPrefStorage().passportOrId = loginResponse.passport_or_id
        SharedPrefStorage().passportOrIdNumber = loginResponse.password_or_id_number

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

        binding.tvNewAccount.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }


    }
}