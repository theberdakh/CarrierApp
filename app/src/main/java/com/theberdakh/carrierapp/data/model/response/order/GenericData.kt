package com.theberdakh.carrierapp.data.model.response.order

data class GenericData<out T>(
    val message: String,
    val data: T
) {
}