package com.example.safeluggpartner.myviewmodels

data class PersonalDetails(
    val businessName : String,
    val ownerName : String,
    val phoneNumber : String,
    val email : String,
)

data class LocationDetails(
    val country : String,
    val state : String,
    val city : String,
    val postalCode : String,
    val streetAddress : String,
    val landmark : String,
    val locationText : String
)