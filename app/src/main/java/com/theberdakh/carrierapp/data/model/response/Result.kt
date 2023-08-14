package com.theberdakh.carrierapp.data.model.response

data class Result(
    val car_number: String,
    val car_photo: String,
    val cargo_type: Int,
    val cargo_unit: Int,
    val cargo_value: Any,
    val date: String,
    val driver_name: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val driver_phone_number: String,
    val id: Int,
    val karer: Int,
    val location: String,
    val weight: String
)