package com.theberdakh.carrierapp.data.model.response.violation

data class PostViolation(
    val karer_name: String,
    val car_photo: String,
    val driver_name: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val driver_phone_number: String,
    val car_number: String,
    val car_brand: String,
    val location: String,
    val cargo_type: String,
    val reason_violation: String,
    var is_updated: Boolean,
    val cargo_date: String,
    val tax_officer: Int
)