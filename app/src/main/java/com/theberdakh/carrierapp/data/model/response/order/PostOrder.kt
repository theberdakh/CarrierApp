package com.theberdakh.carrierapp.data.model.response.order

data class PostOrder(
    val driver_name: String,
    val driver_phone_number: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val car_number: String,
    val car_brand: String,
    var car_photo: String,
    val trailer: String,
    val date: String,
    val trailer_weight: String,
    val direction: String,
    val location: String,
    val cargo_type: Int,
    val cargo_value: String,
    val status: String,
    val stir: String,
    val who: String,
    val violated: Boolean,
    val karer: Int,
    val cargo_unit: Int,
    )