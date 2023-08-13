package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.FragmentLoginBinding
import com.theberdakh.carrierapp.util.checkText

class LoginFragment: Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        initViews()
        initListeners()
    }

    private fun initViews() {
        binding.tvNewAccount.linksClickable = false

        if (binding.etUsername.text.toString().isNotEmpty()&&binding.btnLogin.checkText(binding.etPassword, 8)){
            binding.btnLogin.isEnabled = true
        }
    }

    private fun initListeners() {

        binding.tvNewAccount.setOnClickListener{
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }





    }
}