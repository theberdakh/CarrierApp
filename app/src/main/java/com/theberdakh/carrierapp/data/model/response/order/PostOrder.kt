package com.theberdakh.carrierapp.data.model.response.order

import retrofit2.http.Part
import java.io.File

data class PostOrder(
    val driver_name: String,
    val driver_phone_number: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val car_number: String,
    @Part var car_photo: File,
    val location: String,
    val karer: Int,
    val cargo_type: Int,

    )