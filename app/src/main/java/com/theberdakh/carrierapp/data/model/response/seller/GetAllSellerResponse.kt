package com.theberdakh.carrierapp.data.model.response.seller

data class GetAllSellerResponse(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<GetAllSellerResult>
)