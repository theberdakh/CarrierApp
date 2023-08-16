package com.theberdakh.carrierapp.data.model.response.order

data class Order(
    val count: Int,
    val next: Any?,
    val previous: Any?,
    val results: List<Result>
)