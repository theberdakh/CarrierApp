package com.theberdakh.carrierapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.model.response.order.OrderResponse
import com.theberdakh.carrierapp.data.model.response.violation.ViolationResponse
import com.theberdakh.carrierapp.domain.TaxRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TaxViewModel(private val repository: TaxRepository): ViewModel() {


    val successFlow = MutableSharedFlow<OrderResponse>()
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

    val violationSuccessFlow = MutableSharedFlow<ViolationResponse>()
    val violationMessageFlow = MutableSharedFlow<String>()
    val violationErrorFlow = MutableSharedFlow<Throwable>()

    suspend fun getAllViolations(){

        repository.getAllViolations().onEach {
            when(it){
                is ResultData.Success -> {
                    violationSuccessFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    violationMessageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    violationErrorFlow.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }




}