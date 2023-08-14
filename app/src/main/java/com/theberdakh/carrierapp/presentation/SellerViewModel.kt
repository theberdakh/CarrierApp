package com.theberdakh.carrierapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.carrierapp.data.model.response.LoginBody
import com.theberdakh.carrierapp.data.model.response.LoginResponse
import com.theberdakh.carrierapp.data.model.response.Order
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.domain.auth.SellerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SellerViewModel(private val repository: SellerRepository): ViewModel() {

    val successFlow = MutableSharedFlow<Order>()
    val messageFlow = MutableSharedFlow<String>()
    val errorFlow = MutableSharedFlow<Throwable>()
    suspend fun getAllOrders(){
        repository.getAllOrders().onEach {
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