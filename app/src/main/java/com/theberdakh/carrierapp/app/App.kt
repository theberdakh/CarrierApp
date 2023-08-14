package com.theberdakh.carrierapp.app

import android.app.Application
import com.theberdakh.carrierapp.di.appModule
import com.theberdakh.carrierapp.di.networkModule
import com.theberdakh.carrierapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    companion object{
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this


        startKoin{
            androidContext(this@App)
            modules(listOf(appModule, networkModule, viewModelModule))
        }

    }
}