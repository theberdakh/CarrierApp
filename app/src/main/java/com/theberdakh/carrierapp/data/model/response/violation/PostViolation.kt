package com.theberdakh.carrierapp.data.model.response.violation

data class PostViolation(
    val car_brand: String,
    val car_number: String,
    val cargo_date: String,
    val cargo_type: String,
    val driver_name: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val driver_phone_number: String,
    val karer_name: String,
    val location: String,
    val reason_violation: String,
    val tax_officer: Int
)