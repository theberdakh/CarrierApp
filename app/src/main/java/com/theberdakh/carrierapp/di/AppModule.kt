package com.theberdakh.carrierapp.di

import com.theberdakh.carrierapp.domain.auth.AuthRepository
import com.theberdakh.carrierapp.domain.auth.SellerRepository
import org.koin.dsl.module

val appModule = module {

    single <AuthRepository>{
        AuthRepository(api = get())
    }

    single {
        SellerRepository(api = get())
    }
}