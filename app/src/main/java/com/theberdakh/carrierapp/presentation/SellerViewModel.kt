package com.theberdakh.carrierapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.carrierapp.data.model.response.order.OrderResponse
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.data.model.response.order.PostOrder
import com.theberdakh.carrierapp.data.model.response.order.SortedOrder
import com.theberdakh.carrierapp.domain.auth.SellerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SellerViewModel(private val repository: SellerRepository): ViewModel() {

    //val successFlow = MutableSharedFlow<OrderResponse>()
    val messageFlow = MutableSharedFlow<String>()
    val errorFlow = MutableSharedFlow<Throwable>()


    val postOrderByIdSuccessFlow = MutableSharedFlow<List<Order>>()
    val postOrderByIdMessageFlow = MutableSharedFlow<String>()
    val postOrderByIdErrorFlow = MutableSharedFlow<Throwable>()


     val postOrderSuccessFlow = MutableSharedFlow<Order>()
        val postOrderMessageFlow = MutableSharedFlow<String>()
        val postOrderErrorFlow = MutableSharedFlow<Throwable>()



        suspend fun getAllOrders(){
        repository.getAllOrders().onEach {
            when(it){
                is ResultData.Success -> {
            //        successFlow.emit(it.data)
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
  suspend fun getOrdersById( id : Int ) {
      repository.getOrdersByID(id).onEach {
          when (it) {
              is ResultData.Success -> {
                  postOrderByIdSuccessFlow.emit(it.data)
              }
              is ResultData.Message -> {
                  postOrderByIdMessageFlow.emit(it.message)
              }
              is ResultData.Error -> {
                  postOrderByIdErrorFlow.emit(it.error)
              }
          }
      }.launchIn(viewModelScope)

  }
    suspend fun postOrder(token: String, body: PostOrder){
        repository.postOrder(body).onEach {
            when(it){
                is ResultData.Success -> {
                    postOrderSuccessFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    postOrderMessageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    postOrderErrorFlow.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }


}