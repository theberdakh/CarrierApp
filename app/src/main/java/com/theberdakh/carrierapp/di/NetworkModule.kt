package com.theberdakh.carrierapp.di

import com.theberdakh.carrierapp.data.remote.AuthApi
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single<Retrofit> {
        Retrofit.Builder().baseUrl("http://86.107.197.112/api/").addConverterFactory(get()).build()
    }

    single<Converter.Factory> {
        GsonConverterFactory.create()
    }

    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
    }
}