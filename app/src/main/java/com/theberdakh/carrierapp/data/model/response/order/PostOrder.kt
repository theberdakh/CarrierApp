package com.theberdakh.carrierapp.data.model.response.order

data class PostOrder(
    val driver_name: String,
    val driver_phone_number: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val car_number: String,
    var car_photo: String,
    val location: String,
    val karer: Int,
    val weight: String,
    val cargo_type: Int,
    val cargo_value: Int,
    val cargo_unit: Int,
    )