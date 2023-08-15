package com.theberdakh.carrierapp.data.model.response

data class Order(
    val count: Int,
    val next: Any?,
    val previous: Any?,
    val results: List<Result>
)