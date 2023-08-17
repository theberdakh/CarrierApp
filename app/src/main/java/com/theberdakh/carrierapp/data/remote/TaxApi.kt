package com.theberdakh.carrierapp.data.remote

import com.theberdakh.carrierapp.data.model.response.order.OrderResponse
import com.theberdakh.carrierapp.data.model.response.violation.ViolationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface TaxApi {

    @GET("orders/")
    suspend fun getAllOrders(): Response<OrderResponse>

    @GET("violations/")
    suspend fun getAllViolations(): Response<ViolationResponse>



}