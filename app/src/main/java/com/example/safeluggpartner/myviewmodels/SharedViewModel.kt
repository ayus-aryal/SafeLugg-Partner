package com.example.safeluggpartner.myviewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

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
}