package com.theberdakh.carrierapp.domain

import android.util.Log
import com.theberdakh.carrierapp.data.model.response.login.LoginBody
import com.theberdakh.carrierapp.data.model.response.ResultData
import com.theberdakh.carrierapp.data.model.response.seller.SellerRegisterBody
import com.theberdakh.carrierapp.data.model.response.tax_officer.TaxOfficerRegisterBody
import com.theberdakh.carrierapp.data.remote.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository(private val api: AuthApi) {

    suspend fun login(body: LoginBody)= flow{
        val response = api.login(body)
        if (response.isSuccessful){
            Log.d("AuthRepo", "login request is successful")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("AuthRepo", "login request is message ${response.code()}")
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        Log.d("AuthRepo", "login request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun registerSeller(body: SellerRegisterBody) = flow {
        val response = api.registerSeller(body)
        if (response.isSuccessful && response.body() != null){
            Log.d("AuthRepo", "register request is successful")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("AuthRepo", " register request is message ${response.message()}")
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        Log.d("Seller Register", "request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun registerTaxOfficer(body: TaxOfficerRegisterBody) = flow {
        val response = api.registerTaxOfficer(body)
        if (response.isSuccessful && response.body() != null){
            Log.d("AuthRepo", "register request is successful")
            emit(ResultData.Success(response.body()!!))
        }
        else {
            Log.d("AuthRepo", " register request is message ${response.message()}")
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        Log.d("Seller Register", "request is error")
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)



}