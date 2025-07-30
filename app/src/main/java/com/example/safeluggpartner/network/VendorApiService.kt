package com.example.safeluggpartner.network

import com.example.safeluggpartner.myviewmodels.FcmTokenRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface VendorApiService {

    @Multipart
    @POST("/api/partner/submit")
    suspend fun submitPartnerForm(
        @Part("data") data: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<Unit>



    @GET("api/partner/exists")
    suspend fun checkVendorExists(@Query("email") email: String): Response<Boolean>


    @GET("api/partner/status")
    suspend fun getVerifcationStatus(@Query("email") email: String): Response<Boolean>

    @POST("/api/vendors/{vendorId}/fcm-token")
    fun sendFcmToken(
        @Path("vendorId") vendorID: Long,
        @Body request: FcmTokenRequest
    ): Call<Void>
}

