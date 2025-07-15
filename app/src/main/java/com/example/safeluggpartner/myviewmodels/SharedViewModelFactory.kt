package com.example.safeluggpartner.myviewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.safeluggpartner.network.VendorApiService

class SharedViewModelFactory(
    private val vendorApi: VendorApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(vendorApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
