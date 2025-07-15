package com.example.safeluggpartner.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: VendorApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://c15b9f565266.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VendorApiService::class.java)
    }
}
