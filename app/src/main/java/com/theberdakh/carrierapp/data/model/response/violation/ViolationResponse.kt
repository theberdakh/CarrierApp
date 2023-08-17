package com.theberdakh.carrierapp.data.model.response.violation

data class ViolationResponse(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Violation>
)