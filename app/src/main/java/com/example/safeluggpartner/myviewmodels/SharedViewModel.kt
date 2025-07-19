package com.example.safeluggpartner.myviewmodels

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safeluggpartner.model.FinalSubmissionRequest
import com.example.safeluggpartner.network.VendorApiService
import kotlinx.coroutines.launch

class SharedViewModel(
    private val vendorApi: VendorApiService

) : ViewModel() {

    private val _personalDetails = mutableStateOf<PersonalDetails?>(null)
    val personalDetails: State<PersonalDetails?> = _personalDetails


    fun setUserDetails(details: PersonalDetails){
        _personalDetails.value = details

    }

    private val _locationDetails = mutableStateOf<LocationDetails?>(null)
    val locationDetails: State<LocationDetails?> = _locationDetails


    fun setLocationDetails(details: LocationDetails){
        _locationDetails.value = details

    }


    private val _storageDetails = mutableStateOf<StorageDetails?>(null)
    val storageDetails: State<StorageDetails?> = _storageDetails


    fun setStorageDetails(details: StorageDetails){
        _storageDetails.value = details
    }


    private val _pricingDetails = mutableStateOf<PricingDetails?>(null)
    val pricingDetails : State<PricingDetails?> = _pricingDetails


    fun setPricingDetails(details: PricingDetails){
        _pricingDetails.value = details
    }


    private val _selectedImageUris = mutableStateOf<List<Uri>>(emptyList())
    val selectedImageUris: State<List<Uri>> = _selectedImageUris

    fun addImageUri(uri : Uri){
        _selectedImageUris.value = _selectedImageUris.value + uri
    }

    fun removeImageUri(uri : Uri){
        _selectedImageUris.value = _selectedImageUris.value - uri
    }

    fun clearAllImages() {
        _selectedImageUris.value = emptyList()
    }

    fun getFinalSubmissionRequest(): FinalSubmissionRequest {
        return FinalSubmissionRequest(
            personalDetails = personalDetails.value!!,
            locationDetails = locationDetails.value!!,
            storageDetails = storageDetails.value!!,
            pricingDetails = pricingDetails.value!!
        )
    }

    fun checkEmailExists(email: String, onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            try{
                val response = vendorApi.checkVendorExists(email)
                onResult(response.isSuccessful && response.body() == true)
            }catch(e: Exception){
                onResult(false)
            }
        }
    }

    fun fetchVerificationStatus(email: String, onResult: (Boolean?) -> Unit){
        viewModelScope.launch {
            try{
                val response = vendorApi.getVerifcationStatus(email)
                if(response.isSuccessful){
                    onResult(response.body())
                }else{
                    onResult(null)
                }
            }catch(e: Exception){
                onResult(null)
            }
        }
    }


}

