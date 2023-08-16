package com.theberdakh.carrierapp.data.remote

import com.google.android.gms.nearby.connection.Payload.Type
import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.data.model.response.order.PostOrder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface SellerApi {

    @GET("orders/")
    suspend fun getAllOrders(): Response<Order>


    @GET("orders/by_karer_id/{karer_id}")
    suspend fun getOrdersById(@Path("karer_id") id: Int): Response<Order>

    @POST("orders/create/")
    suspend fun sendOrder(@Header(value = "Authorization") token: String, @Body body: PostOrder)

}