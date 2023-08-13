package com.theberdakh.carrierapp.data.remote

import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    suspend fun login(username: String, password: String)
}