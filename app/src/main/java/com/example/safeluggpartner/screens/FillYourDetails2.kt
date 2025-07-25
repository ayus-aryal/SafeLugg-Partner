package com.example.safeluggpartner.screens

import android.Manifest
import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.safeluggpartner.R
import com.example.safeluggpartner.data.CountriesResponse
import com.example.safeluggpartner.data.parseCountryStateCityJson
import com.example.safeluggpartner.myviewmodels.LocationDetails
import com.example.safeluggpartner.myviewmodels.SharedViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.*

import kotlinx.coroutines.tasks.await
import java.util.*

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FillYourDetails2Screen(navController: NavController, viewModel: SharedViewModel) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }
    val scope = rememberCoroutineScope()

    var countriesResponse by remember { mutableStateOf<CountriesResponse?>(null) }

    LaunchedEffect(Unit) {
        val jsonString = withContext(Dispatchers.IO) {
            context.assets.open("countries_data.json").bufferedReader().use { it.readText() }
        }
        countriesResponse = parseCountryStateCityJson(jsonString)
    }

    var country by rememberSaveable { mutableStateOf("") }
    var state by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var postalCode by rememberSaveable { mutableStateOf("") }
    var streetAddress by rememberSaveable { mutableStateOf("") }
    var landmark by rememberSaveable { mutableStateOf("") }
    var locationText by rememberSaveable { mutableStateOf("No location selected") }

    val countryList = countriesResponse?.countries ?: emptyList()
    val countryNames = countryList.map { it.name }

    val selectedCountryObj = countryList.find { it.name.equals(country, true) }
    val stateList = selectedCountryObj?.states ?: emptyList()
    val stateNames = stateList.map { it.name }

    val selectedStateObj = stateList.find { it.name.equals(state, true) }
    val cityNames = selectedStateObj?.cities ?: emptyList()

    val countryError = country.isBlank()
    val stateError = state.isBlank()
    val cityError = city.isBlank()
    val postalCodeError = postalCode.length < 4
    val streetAddressError = streetAddress.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.logo_safeluggpartner1),
                    contentDescription = "SafeLugg Logo",
                    tint = Color.Black,
                    modifier = Modifier.size(70.dp)
                )
                Text("Where is Your Luggage Space?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Tell us where customers will drop their bags.", fontSize = 14.sp, color = Color.Gray)

                AutoCompleteDropdown("Country *", country, {
                    country = it; state = ""; city = ""
                }, countryError, countryNames)

                AutoCompleteDropdown("State / Province *", state, {
                    state = it; city = ""
                }, stateError, stateNames)

                AutoCompleteDropdown("City *", city, {
                    city = it
                }, cityError, cityNames)

                AddressField("Postal Code *", postalCode, {
                    postalCode = it.filter { ch -> ch.isDigit() }
                }, postalCodeError, KeyboardType.Number)

                MultilineAddressField("Street Address *", streetAddress, {
                    streetAddress = it
                }, streetAddressError)

                AddressField("Landmark (Optional)", landmark, {
                    landmark = it
                }, false)

                Text("Pick GPS Location", fontWeight = FontWeight.Medium, fontSize = 16.sp)

                Button(
                    onClick = {
                        locationPermissionState.launchPermissionRequest()

                        if (locationPermissionState.status.isGranted) {
                            scope.launch {
                                try {
                                    val location = withContext(Dispatchers.IO) {
                                        fusedLocationClient.lastLocation.await()
                                    }
                                    location?.let {
                                        val addresses = withContext(Dispatchers.IO) {
                                            geocoder.getFromLocation(it.latitude, it.longitude, 1)
                                        }
                                        if (!addresses.isNullOrEmpty()) {
                                            val addr = addresses[0]
                                            country = addr.countryName ?: country
                                            state = addr.adminArea ?: state
                                            city = addr.locality ?: city
                                            postalCode = addr.postalCode ?: postalCode
                                            streetAddress = addr.thoroughfare ?: streetAddress
                                            locationText = "GPS: ${addr.getAddressLine(0)}"
                                        } else {
                                            locationText = "Location found but address unavailable"
                                        }
                                    } ?: run {
                                        locationText = "Unable to fetch location. Try moving outdoors."
                                    }
                                } catch (e: Exception) {
                                    locationText = "Error fetching location: ${e.localizedMessage}"
                                }
                            }
                        } else {
                            locationText = "Location permission not granted"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Use My Current Location (GPS)")
                }

                Text(locationText, color = Color.Gray, fontSize = 12.sp)

                val isFormValid = !countryError && !stateError && !cityError && !postalCodeError && !streetAddressError

                Button(
                    onClick = {
                        if (isFormValid) {
                            val locationDetails = LocationDetails(
                                country,
                                state,
                                city,
                                postalCode,
                                streetAddress,
                                landmark,
                                locationText
                            )
                            viewModel.setLocationDetails(locationDetails)
                            navController.navigate("fill_your_details3_screen")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(containerColor = if (isFormValid) Color.Black else Color.Gray)
                ) {
                    Text("Next Step — Storage Details", color = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun AutoCompleteDropdown(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    suggestions: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            isError = isError,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
            singleLine = true
        )
        DropdownMenu(
            expanded = expanded && suggestions.isNotEmpty(),
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            suggestions.filter {
                it.contains(value, ignoreCase = true)
            }.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text(suggestion) },
                    onClick = {
                        onValueChange(suggestion)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddressField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun MultilineAddressField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        maxLines = 3,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

