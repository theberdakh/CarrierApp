package com.theberdakh.carrierapp.data.remote

import com.theberdakh.carrierapp.data.model.response.order.Order
import com.theberdakh.carrierapp.data.model.response.order.OrderResponse
import com.theberdakh.carrierapp.data.model.response.order.PostOrder
import com.theberdakh.carrierapp.data.model.response.order.ResponsePostOrder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SellerApi {

    @GET("orders/")
    suspend fun getAllOrders(): Response<OrderResponse>

    @GET("orders/by_karer_id/{karer_id}")
    suspend fun getOrdersById(@Path("karer_id") id: Int): Response<List<Order>>

    @POST("orders/create/")
    suspend fun postOrder(@Body body: PostOrder): Response<ResponsePostOrder>

}