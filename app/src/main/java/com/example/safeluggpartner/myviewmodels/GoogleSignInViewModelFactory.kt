package com.example.safeluggpartner.myviewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.safeluggpartner.network.VendorApiService

class GoogleSignInViewModelFactory(private val vendorApiService: VendorApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoogleSignInViewModel::class.java)) {
            return GoogleSignInViewModel(vendorApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
