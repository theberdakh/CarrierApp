package com.theberdakh.carrierapp.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.data.model.response.order.OrderResponse
import com.theberdakh.carrierapp.data.model.response.seller.GetAllSellerResponse
import com.theberdakh.carrierapp.data.model.response.violation.PostUpdateViolation
import com.theberdakh.carrierapp.data.model.response.violation.PostViolation
import com.theberdakh.carrierapp.data.model.response.violation.PostViolationGeneral
import com.theberdakh.carrierapp.data.model.response.violation.Violation
import com.theberdakh.carrierapp.data.model.response.violation.ViolationByUnique
import com.theberdakh.carrierapp.data.model.response.violation.ViolationResponse
import com.theberdakh.carrierapp.domain.TaxRepository
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaxViewModel(private val repository: TaxRepository): ViewModel() {


    val successFlow = MutableSharedFlow<List<Order>>()
    val messageFlow = MutableSharedFlow<String>()
    val errorFlow = MutableSharedFlow<Throwable>()





    suspend fun getAllOrders(){

        repository.getAllOrders().onEach {
            when(it){
                is ResultData.Success -> {
                    successFlow.emit(it.data.results)
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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getOrdersByDay() {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val current = LocalDateTime.now().format(formatter)
        makeToast(current)
        repository.getAllOrders().onEach {
            when(it){
                is ResultData.Success -> {
                    val sortedOrders = mutableListOf<Order>()
                    for (order in it.data.results){
                        makeToast("${order.date}")
                        if (order.date.startsWith(current)){
                            sortedOrders.add(order)
                        }
                    }
                    successFlow.emit(sortedOrders)
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

    val violationSuccessFlow = MutableSharedFlow<List<Violation>>()
    val violationMessageFlow = MutableSharedFlow<String>()
    val violationErrorFlow = MutableSharedFlow<Throwable>()

    suspend fun getAllViolations(){

        repository.getAllViolations().onEach {
            when(it){
                is ResultData.Success -> {
                    Log.d("All V", "Success ${it.data}")
                    violationSuccessFlow.emit(it.data.results)
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

    suspend fun getNotEnteredViolations(){

        repository.getAllViolations().onEach { violationResponse ->
            when(violationResponse){
                is ResultData.Success -> {
                    violationResponse.data.results.onEach {
                        if (it.reason_violation == "not_entered" || it.reason_violation == "Mag'liwmat kiritilmegen"){
                            violationSuccessFlow.emit(violationResponse.data.results)
                        }
                    }
                }
                is ResultData.Message -> {
                    Log.d("All V", "Success ${violationResponse.message}")
                    violationMessageFlow.emit(violationResponse.message)

                }
                is ResultData.Error -> {
                    Log.d("All V", "Success ${violationResponse.error}")
                    violationErrorFlow.emit(violationResponse.error)
                }
            }

        }.launchIn(viewModelScope)
    }

    suspend fun getEnteredIncorrectViolations(){

        repository.getAllViolations().onEach { violationResponse ->
            when(violationResponse){
                is ResultData.Success -> {
                 /*   val filteredList = violationResponse.data.results.filter {
                        it.reason_violation == "entered_incorrect" || it.reason_violation == "Mag'liwmat toliq emes"
                    }
                    violationSuccessFlow.emit(filteredList)*/
                }
                is ResultData.Message -> {
                    Log.d("All V", "Success ${violationResponse.message}")
                    violationMessageFlow.emit(violationResponse.message)

                }
                is ResultData.Error -> {
                    Log.d("All V", "Success ${violationResponse.error}")
                    violationErrorFlow.emit(violationResponse.error)
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

    val postUpdatedViolationSuccessFlow = MutableSharedFlow<PostUpdateViolation>()
    val postUpdatedViolationMessageFlow = MutableSharedFlow<String>()
    val postUpdatedViolationErrorFlow = MutableSharedFlow<Throwable>()

    suspend fun postUpdatedViolation(postUpdateViolation: PostUpdateViolation){


      repository.addNewUpdatedViolation(postUpdateViolation).onEach {
            when(it){
                is ResultData.Success -> {
                    postUpdatedViolationSuccessFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    postUpdatedViolationMessageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    postUpdatedViolationErrorFlow.emit(it.error)
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


    val singleViolationSuccessFlow = MutableSharedFlow<Violation>()
    val singleViolationMessage = MutableSharedFlow<String>()
    val singleViolationError = MutableSharedFlow<Throwable>()

    suspend fun getViolationById(id: Int) {
        repository.getViolationByID(id).onEach {
            when(it){
                is ResultData.Success -> {
                    singleViolationSuccessFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    singleViolationMessage.emit(it.message)
                }
                is ResultData.Error -> {
                    singleViolationError.emit(it.error)
                }
            }

        }.launchIn(viewModelScope)
    }

    val uniqueViolationSuccessFlow = MutableSharedFlow<List<ViolationByUnique>>()
    val uniqueViolationMessage = MutableSharedFlow<String>()
    val uniqueViolationError = MutableSharedFlow<Throwable>()


    suspend fun getViolationByUnique(uniqueNumber: Int) {
        repository.getViolationByUniqueNumber(uniqueNumber).onEach {
            when(it){
                is ResultData.Success -> {
                    uniqueViolationSuccessFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    uniqueViolationMessage.emit(it.message)
                }
                is ResultData.Error -> {
                    uniqueViolationError.emit(it.error)
                }
            }

        }.launchIn(viewModelScope)
    }




}