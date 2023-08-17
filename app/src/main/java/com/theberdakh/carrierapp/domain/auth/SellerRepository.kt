package com.theberdakh.carrierapp.domain.auth

import android.util.Log
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.model.response.order.PostOrder
import com.theberdakh.carrierapp.data.remote.SellerApi
import com.theberdakh.carrierapp.util.makeToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SellerRepository(private val api: SellerApi) {

    suspend fun getOrdersByID( id: Int)= flow{
        val response = api.getOrdersById(id)
        if (response.isSuccessful && response.body() != null){
            Log.d("SellerRepo", "by id request is successful")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("SellerRepo", "by id request is message")
            emit(ResultData.Message(response.code().toString()))

        }
    }.catch {
        Log.d("SellerRepo", "by request is error")
        it.printStackTrace()
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun postOrder(body: PostOrder)= flow{

        Log.d("Token", "post order repo")
        val response = api.postOrder(body)
        if (response.isSuccessful && response.body() != null){
            Log.d("SellerRepo", "post request is successful")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("Tag", response.body().toString())
            Log.d("Tag", response.message().toString())
            Log.d("Tag", response.code().toString())
            Log.d("Tag", response.headers().toString())
            Log.d("Tag", response.raw().toString())
            Log.d("SellerRepo", "post request is ${response.code()}")
            emit(ResultData.Message(response.code().toString()))
        }
    }.catch {
        Log.d("SellerRepo", "post request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)




}