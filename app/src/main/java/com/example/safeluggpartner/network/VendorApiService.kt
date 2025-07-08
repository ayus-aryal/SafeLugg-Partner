package com.example.safeluggpartner.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface VendorApiService {

    @Multipart
    @POST("/api/partner/submit")
    suspend fun submitPartnerForm(
        @Part("data") data: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<Unit>
}

