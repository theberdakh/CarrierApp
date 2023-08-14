package com.theberdakh.carrierapp.di

import com.theberdakh.carrierapp.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<LoginViewModel> {
        LoginViewModel(repository = get())
    }
}