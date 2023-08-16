package com.theberdakh.carrierapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.model.response.seller.SellerRegisterBody
import com.theberdakh.carrierapp.data.model.response.seller.SellerResponse
import com.theberdakh.carrierapp.domain.auth.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RegisterViewModel(private val repository: AuthRepository): ViewModel() {
    val successFlow = MutableSharedFlow<SellerResponse>()
    val messageFlow = MutableSharedFlow<String>()
    val errorFlow = MutableSharedFlow<Throwable>()
    suspend fun registerSeller(sellerName: String, phoneNumber: String, password: String, password2: String){
        repository.registerSeller(SellerRegisterBody(phoneNumber, sellerName, password, password2 )).onEach {
            Log.d("Register View Model click", "$phoneNumber")

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