package com.theberdakh.carrierapp.util

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {

    fun getLocation(): Flow<Location>

    class LocationException(message: String): Exception()
}