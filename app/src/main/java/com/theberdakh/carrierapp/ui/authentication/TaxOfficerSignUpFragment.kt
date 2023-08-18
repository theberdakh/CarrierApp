package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.databinding.TaxOfficerSignUpBinding
import com.theberdakh.carrierapp.presentation.RegisterViewModel
import com.theberdakh.carrierapp.util.makeToast
import com.theberdakh.carrierapp.util.showSnackBar
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks

class TaxOfficerSignUpFragment: Fragment(R.layout.tax_officer_sign_up) {
    private lateinit var binding: TaxOfficerSignUpBinding
    private val viewModel by viewModel<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TaxOfficerSignUpBinding.bind(view)

        initObservers()
        initViews()
        initListeners()


    }

    private fun initObservers() {
        viewModel.successTaxFlow.onEach {
            Log.d("Login Success", "Success ${it.token}")
            showSnackBar(binding.root, "Akkount registratciya etildi.")
            findNavController().popBackStack()
        }.launchIn(lifecycleScope)

        viewModel.messageTaxFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.errorTaxFlow.onEach {
            makeToast("Error")
        }.launchIn(lifecycleScope)

    }

    private fun initViews() {
        binding.switchId.isChecked = true
        binding.btnRegister.isEnabled = false
    }

    private fun initListeners() {




        binding.switchId.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.text = if (isChecked) "ID" else "Passport"
        }

        binding.btnRegister.clicks().debounce(300).onEach {
            makeToast("Clicked")

            viewModel.registerTaxOfficer(
                fullName = binding.etFirstLastPoetricName.text.toString(),
                phoneNumber = "+${998}${binding.etPhoneNumber.text.toString()}",
                passportOrId = if (binding.switchId.isChecked) "document_id" else "passport",
                passportOrIdSeries = binding.etPassportSeries.text.toString(),
                position = binding.etPosition.text.toString(),
                 workingRegion = 1,
                password = binding.etPassword.text.toString(),
                password2 = binding.etPassword.text.toString()
            )
        }.launchIn(lifecycleScope)
    }


}