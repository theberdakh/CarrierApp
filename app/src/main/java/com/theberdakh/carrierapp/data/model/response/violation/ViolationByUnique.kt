package com.theberdakh.carrierapp.data.model.response.violation

data class ViolationByUnique(
    val car_brand: String,
    val car_number: String,
    val car_photo: String,
    val cargo_date: String,
    val cargo_type: String,
    val created_at: String,
    val driver_name: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val driver_phone_number: String,
    val id: Int,
    val is_updated: Boolean,
    val karer_name: String,
    val location: String,
    val reason_violation: String,
    val tax_officer: Int,
    val unique_number: Int
)