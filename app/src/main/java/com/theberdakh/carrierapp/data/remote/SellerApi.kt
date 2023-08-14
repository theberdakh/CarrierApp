package com.theberdakh.carrierapp.data.remote

import com.theberdakh.carrierapp.data.model.response.LoginBody
import com.theberdakh.carrierapp.data.model.response.LoginResponse
import com.theberdakh.carrierapp.data.model.response.Order
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SellerApi {

    @POST("orders/")
    suspend fun getAllOrders(): Response<Order>

}