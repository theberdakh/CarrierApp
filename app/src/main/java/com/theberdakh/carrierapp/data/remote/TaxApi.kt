package com.theberdakh.carrierapp.data.remote

import com.theberdakh.carrierapp.data.model.response.order.OrderResponse
import com.theberdakh.carrierapp.data.model.response.seller.GetAllSellerResponse
import com.theberdakh.carrierapp.data.model.response.violation.PostViolation
import com.theberdakh.carrierapp.data.model.response.violation.Violation
import com.theberdakh.carrierapp.data.model.response.violation.ViolationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TaxApi {

    @GET("orders/")
    suspend fun getAllOrders(): Response<OrderResponse>

    @GET("violations/")
    suspend fun getAllViolations(): Response<ViolationResponse>

    @POST("violations/create/")
    suspend fun postViolation(@Body body: PostViolation): Response<Violation>

    @GET("karer/")
    suspend fun getAllSellers(): Response<GetAllSellerResponse>

}