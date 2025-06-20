package com.example.safeluggpartner.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: VendorApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Replace with actual URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VendorApiService::class.java)
    }
}
