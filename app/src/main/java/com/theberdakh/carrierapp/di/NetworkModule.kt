package com.theberdakh.carrierapp.di

import com.theberdakh.carrierapp.data.local.SharedPrefStorage
import com.theberdakh.carrierapp.data.remote.AuthApi
import com.theberdakh.carrierapp.data.remote.SellerApi
import com.theberdakh.carrierapp.data.remote.TaxApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {


    fun provideRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        )


        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor { chain: Interceptor.Chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Token 05bcc202a30b9c6342e5e09a8133792025ee927d")
                    .build()

                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://86.107.197.112/api/")
            .client(client)
            .build()
    }

    single {
        provideRetrofit()
    }

    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
    }

    single<SellerApi> {
        get<Retrofit>().create(SellerApi::class.java)
    }

    single<TaxApi> {
        get<Retrofit>().create(TaxApi::class.java)
    }

}