package com.theberdakh.carrierapp.di

import com.theberdakh.carrierapp.domain.AuthRepository
import com.theberdakh.carrierapp.domain.SellerRepository
import com.theberdakh.carrierapp.domain.TaxRepository
import org.koin.dsl.module

val appModule = module {

    single <AuthRepository>{
        AuthRepository(api = get())
    }

    single {
        SellerRepository(api = get())
    }

    single {
        TaxRepository(api = get())
    }
}