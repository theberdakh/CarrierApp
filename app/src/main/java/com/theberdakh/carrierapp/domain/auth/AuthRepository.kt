package com.theberdakh.carrierapp.domain.auth

import android.util.Log
import com.theberdakh.carrierapp.data.model.User
import com.theberdakh.carrierapp.data.model.response.LoginBody
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.remote.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository(private val api: AuthApi) {

    suspend fun login(body: LoginBody)= flow{
        val response = api.login(body)
        if (response.isSuccessful && response.body() != null){
            Log.d("AuthRepo", "request is successful")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("AuthRepo", "request is message")
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        Log.d("AuthRepo", "request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)




}