package com.theberdakh.carrierapp.data.model.response.order


data class Result(
    val id: Int,
    val driver_name: String,
    val driver_phone_number: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val car_number: String,
    val car_photo: String,
    val location: String,
    val cargo_value: Any?,
    val weight: String,
    val date: String,
    val karer: Int,
    val cargo_type: Int,
    val cargo_unit: Int,
)