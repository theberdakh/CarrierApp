package com.theberdakh.carrierapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.data.model.response.order.PostOrder
import com.theberdakh.carrierapp.data.model.response.order.ResponsePostOrder
import com.theberdakh.carrierapp.domain.SellerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SellerViewModel(private val repository: SellerRepository): ViewModel() {


    val postOrderByIdSuccessFlow = MutableSharedFlow<List<Order>>()
    val postOrderByIdMessageFlow = MutableSharedFlow<String>()
    val postOrderByIdErrorFlow = MutableSharedFlow<Throwable>()


     val postOrderSuccessFlow = MutableSharedFlow<ResponsePostOrder>()
        val postOrderMessageFlow = MutableSharedFlow<String>()
        val postOrderErrorFlow = MutableSharedFlow<Throwable>()




  suspend fun getOrdersBySellerId(id : Int ) {
      repository.getOrdersBySellerID(id).onEach {
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
    suspend fun postOrder( body: PostOrder){
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


    val orderSuccessFlow = MutableSharedFlow<Order>()
    val orderMessageFlow = MutableSharedFlow<String>()
    val orderErrorFlow = MutableSharedFlow<Throwable>()
    suspend fun getOrderById(id: Int){
        repository.getOrderDetails(id).onEach {
            when(it){
                is ResultData.Success -> {
                    orderSuccessFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    orderMessageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    orderErrorFlow.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }


}