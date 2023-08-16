package com.theberdakh.carrierapp.data.model.response.order

data class PostOrder(
    val car_number: String,
    val cargo_type: Int,
    val cargo_unit: Int,
    val cargo_value: String,
    val driver_name: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val driver_phone_number: String,
    val karer: Int,
    val location: String,
    val weight: String
)