package com.theberdakh.carrierapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.model.response.order.OrderResponse
import com.theberdakh.carrierapp.data.model.response.seller.GetAllSellerResponse
import com.theberdakh.carrierapp.data.model.response.violation.PostViolation
import com.theberdakh.carrierapp.data.model.response.violation.Violation
import com.theberdakh.carrierapp.data.model.response.violation.ViolationResponse
import com.theberdakh.carrierapp.domain.TaxRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
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
                    Log.d("All V", "Success ${it.data}")
                    violationSuccessFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    Log.d("All V", "Success ${it.message}")
                    violationMessageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    Log.d("All V", "Success ${it.error}")
                    violationErrorFlow.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }

    val postViolationSuccessFlow = MutableSharedFlow<PostViolation>()
    val postViolationMessageFlow = MutableSharedFlow<String>()
    val postViolationErrorFlow = MutableSharedFlow<Throwable>()

    suspend fun postViolation(postViolation: PostViolation){


      repository.postViolation(postViolation).onEach {
            when(it){
                is ResultData.Success -> {
                    postViolationSuccessFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    postViolationMessageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    postViolationErrorFlow.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }


    val allSellersSuccessFlow = MutableSharedFlow<GetAllSellerResponse>()
    val allSellersMessageFlow = MutableSharedFlow<String>()
    val allSellersErrorFlow = MutableSharedFlow<Throwable>()

    suspend fun getAllSellers(){


        repository.getAllSellers().onEach {
            when(it){
                is ResultData.Success -> {
                    allSellersSuccessFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    allSellersMessageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    allSellersErrorFlow.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }






}