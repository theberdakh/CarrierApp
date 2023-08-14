package com.theberdakh.carrierapp.data.model.response

data class LoginResponse(
    val id: Int,
    val type: String,
    val phone_number: String,
    val token: String,
    val kareer_name: String
)
