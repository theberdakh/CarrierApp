package com.theberdakh.carrierapp.data.model.response.tax_officer

data class TaxOfficer(
    val full_name: String,
    val passport_or_id: String,
    val password_or_id_number: String,
    val phone_number: String,
    val position: String,
    val working_region: Int
)