package com.example.safeluggpartner.screens

import android.Manifest
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import java.util.*

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FillYourDetails2Screen(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val geocoder = Geocoder(context, Locale.getDefault())

    // Form States
    var country by rememberSaveable { mutableStateOf("") }
    var state by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var postalCode by rememberSaveable { mutableStateOf("") }
    var streetAddress by rememberSaveable { mutableStateOf("") }
    var landmark by rememberSaveable { mutableStateOf("") }
    var locationText by rememberSaveable { mutableStateOf("No location selected") }

    // Validation
    val countryError = country.isBlank()
    val stateError = state.isBlank()
    val cityError = city.isBlank()
    val postalCodeError = postalCode.length < 4
    val streetAddressError = streetAddress.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        AddressField("Country *", country, { country = it }, countryError)
        AddressField("State / Province *", state, { state = it }, stateError)
        AddressField("City *", city, { city = it }, cityError)
        AddressField("Postal Code *", postalCode, { postalCode = it.filter { it.isDigit() } }, postalCodeError, KeyboardType.Number)
        MultilineAddressField("Street Address *", streetAddress, { streetAddress = it }, streetAddressError)
        AddressField("Landmark (Optional)", landmark, { landmark = it }, false)

        Spacer(Modifier.height(24.dp))

        Text("Pick GPS Location", fontWeight = FontWeight.Medium, fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                locationPermissionState.launchPermissionRequest()
                if (locationPermissionState.status.isGranted) {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            location?.let {
                                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
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
                                locationText = "Unable to fetch location"
                            }
                        }
                        .addOnFailureListener {
                            locationText = "Error fetching location: ${it.localizedMessage}"
                        }
                } else {
                    locationText = "Location permission not granted"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Use My Current Location (GPS)")
        }

        Spacer(Modifier.height(8.dp))
        Text(locationText, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                if (!countryError && !stateError && !cityError && !postalCodeError && !streetAddressError) {
                    navController.navigate("home_screen") // Replace with next screen
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Next Step", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun AddressField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(label, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = error,
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )
        if (error) {
            Text("$label is required", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }
    }
}

@Composable
fun MultilineAddressField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean
) {
    Column(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(label, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder = { Text("Enter full address") },
            isError = error,
            maxLines = 4,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle.Default.copy(fontSize = 14.sp),
            shape = RoundedCornerShape(8.dp)
        )
        if (error) {
            Text("$label is required", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FillYourDetails2ScreenPreview() {
    FillYourDetails2Screen(navController = rememberNavController())
}
