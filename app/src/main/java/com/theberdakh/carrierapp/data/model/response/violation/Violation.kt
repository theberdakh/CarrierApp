package com.theberdakh.carrierapp.data.model.response.violation

data class Violation(
    val car_number: String,
    val car_photo: String,
    val cargo_date: String,
    val cargo_type: Int,
    val created_at: String,
    val driver_name: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val driver_phone_number: String,
    val id: Int,
    val location: String,
    val reason_violation: String,
    val tax_officer: Int
)