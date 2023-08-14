package com.theberdakh.carrierapp.domain.auth

import android.util.Log
import com.theberdakh.carrierapp.data.model.response.LoginBody
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.remote.SellerApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SellerRepository(private val api: SellerApi) {
    suspend fun getAllOrders()= flow{
        val response = api.getAllOrders()
        if (response.isSuccessful && response.body() != null){
            Log.d("SellerRepo", "request is successful")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("SellerRepo", "request is message")
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        Log.d("SellerRepo", "request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)


}