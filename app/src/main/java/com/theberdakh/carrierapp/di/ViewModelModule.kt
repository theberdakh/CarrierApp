package com.theberdakh.carrierapp.di

import com.theberdakh.carrierapp.presentation.LoginViewModel
import com.theberdakh.carrierapp.presentation.RegisterViewModel
import com.theberdakh.carrierapp.presentation.SellerViewModel
import com.theberdakh.carrierapp.presentation.TaxViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<LoginViewModel> {
        LoginViewModel(repository = get())
    }
    viewModel<SellerViewModel>{
        SellerViewModel(repository = get())
    }

    viewModel<RegisterViewModel>{
        RegisterViewModel(repository = get())
    }

    viewModel<TaxViewModel>{
        TaxViewModel(repository = get())
    }
}