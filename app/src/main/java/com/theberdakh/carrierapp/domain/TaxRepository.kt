package com.theberdakh.carrierapp.domain

import android.util.Log
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.model.response.violation.PostViolation
import com.theberdakh.carrierapp.data.remote.TaxApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TaxRepository(private val api: TaxApi) {

    suspend fun getAllOrders()= flow{
        val response = api.getAllOrders()
        if (response.isSuccessful && response.body() != null){
            Log.d("SellerRepo", "request is successful all orders")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("SellerRepo", " all orders request is ${response.body()}")

            emit(ResultData.Message(response.body().toString()))
        }
    }.catch {
        Log.d("SellerRepo", " all orders request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)


    suspend fun getAllViolations()= flow{
        val response = api.getAllViolations()
        if (response.isSuccessful && response.body() != null){
            Log.d("SellerRepo", "request is successful all orders")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("SellerRepo", " all orders request is ${response.body()}")

            emit(ResultData.Message(response.body().toString()))
        }
    }.catch {
        Log.d("SellerRepo", " all orders request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)


    suspend fun postViolation(postViolation: PostViolation) = flow {
        val response  = api.postViolation(postViolation)
        if (response.isSuccessful && response.body() != null){
            Log.d("SellerRepo", "post is successful post")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("SellerRepo", " all orders request is ${response.body()}")

            emit(ResultData.Message(response.body().toString()))
        }
    }.catch {
        Log.d("SellerRepo", " all orders request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllSellers() = flow {
        val response  = api.getAllSellers()
        if (response.isSuccessful && response.body() != null){
            Log.d("SellerRepo", "post is successful post")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("SellerRepo", " all orders request is ${response.body()}")

            emit(ResultData.Message(response.body().toString()))
        }
    }.catch {
        Log.d("SellerRepo", " all orders request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

}