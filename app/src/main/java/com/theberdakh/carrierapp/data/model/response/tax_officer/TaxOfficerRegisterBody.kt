package com.theberdakh.carrierapp.data.model.response.tax_officer

data class TaxOfficerRegisterBody(
    val phone_number: String,
    val full_name: String,
    val passport_or_id: String,
    val password_or_id_number: String,
    val position: String,
    val working_region: Int,
    val password: String,
    val password2: String,
)
