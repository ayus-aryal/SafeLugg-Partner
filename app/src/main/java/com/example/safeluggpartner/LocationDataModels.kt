// LocationDataModel.kt
import kotlinx.serialization.Serializable

@Serializable
data class CountryResponse(
    val countries: List<Country>
)

@Serializable
data class Country(
    val name: String,
    val states: List<State>
)

@Serializable
data class State(
    val name: String,
    val cities: List<String>
)
