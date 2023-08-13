package com.theberdakh.carrierapp.di

import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin

val networkModule = module {

    single<Retrofit> {
        Retrofit.Builder().baseUrl("86.107.197.112/api").addConverterFactory(get()).build()
    }

    single<Converter.Factory> {
        GsonConverterFactory.create()
    }
}