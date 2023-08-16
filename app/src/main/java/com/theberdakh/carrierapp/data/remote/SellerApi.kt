package com.theberdakh.carrierapp.data.remote

import com.theberdakh.carrierapp.data.model.response.order.Order
import retrofit2.Response
import retrofit2.http.GET

interface SellerApi {

    @GET("orders/")
    suspend fun getAllOrders(): Response<Order>

}