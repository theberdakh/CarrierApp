package com.theberdakh.carrierapp.data.model.response.order

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val id: Int,
    val driver_name: String,
    val driver_phone_number: String,
    val driver_passport_or_id: String,
    val driver_passport_or_id_number: String,
    val car_number: String,
    val car_photo: String,
    val location: String,
    val cargo_value: String,
    val weight: String,
    val date: String,
    val karer: Int,
    val cargo_type: Int,
    val cargo_unit: Int,
): Parcelable