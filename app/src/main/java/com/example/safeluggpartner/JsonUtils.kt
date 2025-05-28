package com.example.safeluggpartner

import CountryResponse

// JsonUtils.kt
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object JsonUtils {
    private val json = Json { ignoreUnknownKeys = true } // ignores unknown fields

    fun parseCountryResponse(jsonString: String): CountryResponse {
        return json.decodeFromString(jsonString)
    }
}

