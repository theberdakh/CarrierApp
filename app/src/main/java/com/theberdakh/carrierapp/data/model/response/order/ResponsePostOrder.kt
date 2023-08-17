package com.theberdakh.carrierapp.data.model.response.order

data class ResponsePostOrder(
    val id: Int,
    val driver_photo: String,
    val driver_name: String,
    val driver_phone_number: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val car_number: String,
    val location: String,
    val cargo_value: String?,
    val weight: String

)
