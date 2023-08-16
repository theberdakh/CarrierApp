package com.theberdakh.carrierapp.data.model.response.seller

data class SellerRegisterBody(
    val phone_number: String,
    val karer_name: String,
    val password: String,
    val password2: String
)
