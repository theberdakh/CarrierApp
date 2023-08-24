package com.theberdakh.carrierapp.data.model.response.login

data class LoginResponse(
    val id: Int,
    val type: String,
    val phone_number: String,
    val token: String,
    val karer_name: String,
    val full_name: String,
    val passport_or_id: String,
    val password_or_id_number: String,
    val position: String,
    val working_region: String
)
