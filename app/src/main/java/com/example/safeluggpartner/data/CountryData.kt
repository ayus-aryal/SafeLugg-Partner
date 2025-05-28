package com.example.safeluggpartner.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class CountriesResponse(
    @SerialName("countries") val countries: List<CountryData>
)

@Serializable
data class CountryData(
    @SerialName("name") val name: String,
    @SerialName("states") val states: List<StateData>
)

@Serializable
data class StateData(
    @SerialName("name") val name: String,
    @SerialName("cities") val cities: List<String>
)

fun parseCountryStateCityJson(jsonString: String): CountriesResponse {
    return Json { ignoreUnknownKeys = true }.decodeFromString(
        CountriesResponse.serializer(), jsonString
    )
}
