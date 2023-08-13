package com.theberdakh.carrierapp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.theberdakh.carrierapp.app.App
import com.theberdakh.carrierapp.util.StringPreference

class SharedPrefStorage {

    companion object{
        private const val CARRIER_SHARED_PREF = "CarrierSharedPref"

        val pref: SharedPreferences = App.instance.getSharedPreferences(CARRIER_SHARED_PREF, Context.MODE_PRIVATE)
    }


    var phoneNumber by StringPreference(pref)

}