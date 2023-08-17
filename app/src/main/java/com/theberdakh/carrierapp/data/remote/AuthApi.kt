package com.theberdakh.carrierapp.data.remote

import com.theberdakh.carrierapp.data.model.response.login.LoginBody
import com.theberdakh.carrierapp.data.model.response.login.LoginResponse
import com.theberdakh.carrierapp.data.model.response.seller.SellerRegisterBody
import com.theberdakh.carrierapp.data.model.response.seller.SellerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {


    @POST("auth/login/")
    suspend fun login(@Body body: LoginBody): Response<LoginResponse>

    @POST("auth/register/karer/")
    suspend fun registerSeller(@Body body: SellerRegisterBody): Response<SellerResponse>


}