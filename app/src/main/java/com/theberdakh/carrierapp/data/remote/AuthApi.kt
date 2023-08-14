package com.theberdakh.carrierapp.data.remote

import com.theberdakh.carrierapp.data.model.response.LoginBody
import com.theberdakh.carrierapp.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login/")
    suspend fun login( @Body body: LoginBody): Response<LoginResponse>


}