package com.theberdakh.carrierapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.carrierapp.data.model.response.LoginBody
import com.theberdakh.carrierapp.data.model.response.LoginResponse
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.domain.auth.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    val successFlow = MutableSharedFlow<LoginResponse>()
    val messageFlow = MutableSharedFlow<String>()
    val errorFlow = MutableSharedFlow<Throwable>()
    suspend fun login(phone: String, password: String){
        repository.login(LoginBody( "+${998}$phone", password)).onEach {
            Log.d("Login click", "+${998}$phone, $password")

            when(it){
                is ResultData.Success -> {
                    successFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    messageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    errorFlow.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }


}