package com.example.safeluggpartner.model

import com.example.safeluggpartner.myviewmodels.*

data class FinalSubmissionRequest(
    val personalDetails: PersonalDetails,
    val locationDetails: LocationDetails,
    val storageDetails: StorageDetails,
    val pricingDetails: PricingDetails
)
