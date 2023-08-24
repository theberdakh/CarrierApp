package com.theberdakh.carrierapp.ui.authentication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.theberdakh.carrierapp.R
import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.model.response.login.LoginResponse
import com.theberdakh.carrierapp.data.model.response.tax_officer.TaxOfficerRegisterResponse
import com.theberdakh.carrierapp.databinding.TaxOfficerSignUpBinding
import com.theberdakh.carrierapp.presentation.RegisterViewModel
import com.theberdakh.carrierapp.util.getNotNullText
import com.theberdakh.carrierapp.util.hasText
import com.theberdakh.carrierapp.util.makeToast
import com.theberdakh.carrierapp.util.setCustomAdapter
import com.theberdakh.carrierapp.util.setErrorText
import com.theberdakh.carrierapp.util.showSnackBar
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.view.clicks
import kotlin.math.log

class TaxOfficerSignUpFragment: Fragment(R.layout.tax_officer_sign_up) {
    private lateinit var binding: TaxOfficerSignUpBinding
    private val viewModel by viewModel<RegisterViewModel>()
    private var phone = ""
    private var password = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = TaxOfficerSignUpBinding.bind(view)

        initObservers()
        initViews()
        initListeners()


    }

    private fun initObservers() {

        viewModel.loginSuccessFlow.onEach {
            login(it)
            makeToast("Login success")
           findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToTaxFragment())
        }.launchIn(lifecycleScope)

        viewModel.successTaxFlow.onEach {
            Log.d("Login Success", "Success ${it.token}")
            showSnackBar(binding.root, "Akkount registratciya etildi.")
            viewModel.login(phone = phone, password = password)


        }.launchIn(lifecycleScope)

        viewModel.messageTaxFlow.onEach {
            makeToast(it)
        }.launchIn(lifecycleScope)

        viewModel.errorTaxFlow.onEach {
            makeToast("Error")
        }.launchIn(lifecycleScope)

    }

    private fun login(loginResponse: LoginResponse) {

        SharedPrefStorage().id = loginResponse.id
        SharedPrefStorage().type = loginResponse.type
        SharedPrefStorage().phoneNumber = phone
        SharedPrefStorage().token = loginResponse.token
        SharedPrefStorage().name = loginResponse.full_name
        SharedPrefStorage().region = loginResponse.working_region
        SharedPrefStorage().position = loginResponse.position
        SharedPrefStorage().password = password
        SharedPrefStorage().signedIn = true
        SharedPrefStorage().passportOrId = loginResponse.passport_or_id
        SharedPrefStorage().passportOrIdNumber = loginResponse.password_or_id_number

    }

    private fun initViews() {

        binding.btnRegister.isEnabled = false
        binding.atvPosition.setCustomAdapter(listOf("Basliq", "Orinbasar"))
        binding.atvDocumentType.setCustomAdapter(listOf("ID", "Passport"))
        binding.atvRegion.setCustomAdapter(listOf("Qaraqalpaqstan Respublikasi", "Andijan", "Buxara", "Ferg'ana", "Jizzax", "Xorezm", "Namangan", "Nawayi", "Qashqadarya", "Samarqand", "Sirdarya", "Surxandarya", "Tashkent"))
        binding.etFirstLastPoetricName.setErrorText(binding.tilFirstLast, doAfter = true){
            it.toString().isEmpty()
        }

        binding.etFirstLastPoetricName.setErrorText(binding.tilFirstLast, doAfter = true){
            it.toString().isEmpty()
        }
        binding.etPhoneNumber.setErrorText(binding.tilPhoneNumber, doAfter = true){
            it.toString().isEmpty() || it.toString().length < 9
        }

        binding.etPassportSeries.setErrorText(binding.tilPassportSeries, doAfter = true){
            it.toString().isEmpty()
        }
        binding.etPassword.setErrorText(binding.tilPassword, errorText = "Parol 8 belgiden az bolmawi kerek!", doAfter = true){
            it.toString().isEmpty() || it.toString().length < 8
        }


    }

    private fun initListeners() {

        binding.etPassword.addTextChangedListener {
            if (binding.etFirstLastPoetricName.hasText() && binding.etPassportSeries.hasText()
                &&binding.etPhoneNumber.hasText() && binding.etPassword.hasText()){
                binding.btnRegister.isEnabled = true
            }
        }



        binding.btnRegister.clicks().debounce(300).onEach {
            makeToast("Clicked")
            phone = binding.etPhoneNumber.getNotNullText()
            password = binding.etPassword.getNotNullText()

            viewModel.registerTaxOfficer(
                binding.etFirstLastPoetricName.getNotNullText(),
                binding.etPhoneNumber.getNotNullText(),
                binding.atvDocumentType.text.toString(),
                binding.etPassportSeries.getNotNullText(),
                binding.atvPosition.text.toString(),
                binding.atvRegion.text.toString(),
                binding.etPassword.getNotNullText(),
                binding.etPassword.getNotNullText()
            )

        }.launchIn(lifecycleScope)
    }


}