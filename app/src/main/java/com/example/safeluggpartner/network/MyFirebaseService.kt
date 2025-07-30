package com.example.safeluggpartner.network

import android.content.Context
import android.util.Log
import com.example.safeluggpartner.myviewmodels.FcmTokenRequest
import com.google.firebase.messaging.FirebaseMessagingService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseService : FirebaseMessagingService(){

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Refreshed token: $token")

        val vendorId = getVendorIdLocally(applicationContext)
        if (vendorId != -1L) {
            sendTokenToServer(token, vendorId)
        } else {
            Log.e("FCM", "Vendor ID not found in SharedPreferences")
        }
    }


    private fun sendTokenToServer(token: String, vendorId: Long) {
        val request = FcmTokenRequest(vendorId = vendorId, fcmToken = token)

        RetrofitInstance.api.sendFcmToken(vendorId, request)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("FCM", "FCM token sent to backend successfully")
                    } else {
                        Log.e("FCM", "Failed to send token: ${response.code()} - ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("FCM", "Error sending FCM token", t)
                }
            })
    }

    fun getVendorIdLocally(context: Context): Long {
        val sharedPrefs = context.getSharedPreferences("vendor_prefs", Context.MODE_PRIVATE)
        return sharedPrefs.getLong("vendor_id", -1L) // -1 indicates not found
    }



}