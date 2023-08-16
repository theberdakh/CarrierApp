package com.theberdakh.carrierapp.data.remote

import com.theberdakh.carrierapp.data.model.response.order.Order
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SellerApi {

    @GET("orders/")
    suspend fun getAllOrders(): Response<Order>


    @GET("orders/by_karer_id/{karer_id}")
    suspend fun getOrdersById(@Path("karer_id") id: Int): Response<Order>

}