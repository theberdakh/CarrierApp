package com.theberdakh.carrierapp.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.theberdakh.carrierapp.app.App
import java.text.SimpleDateFormat
import java.util.Date

private var locationText = ""

fun getCurrentDate(pattern: String = "dd/M/yyyy hh:mm:ss"): String {
    val sdf = SimpleDateFormat(pattern)
    return sdf.format(Date())
}

fun Activity.checkLocationPermissions() : Boolean {


   return if (ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100
        )
       false
    }
     else {
         true
    }
}

