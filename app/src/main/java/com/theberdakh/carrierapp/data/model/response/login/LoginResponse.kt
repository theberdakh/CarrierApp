package com.theberdakh.carrierapp.data.model.response.login

data class LoginResponse(
    val id: Int,
    val type: String,
    val phone_number: String,
    val token: String,
    val karer_name: String
)
