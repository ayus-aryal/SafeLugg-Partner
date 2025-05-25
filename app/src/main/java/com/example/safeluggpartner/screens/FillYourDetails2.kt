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
fun FillYourDetails2Screen(
    navController: NavController,
    onNextClicked: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    var address by rememberSaveable { mutableStateOf("") }
    var locationText by rememberSaveable { mutableStateOf("No location selected") }

    val addressError = address.isBlank()

    val geocoder = Geocoder(context, Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Business Address *", fontWeight = FontWeight.Medium, fontSize = 16.sp)
        Spacer(Modifier.height(4.dp))
        TextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Enter full address") },
            isError = addressError,
            maxLines = 4,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle.Default.copy(fontSize = 14.sp),
            shape = RoundedCornerShape(8.dp)
        )
        if (addressError) {
            Text("Address is required", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

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
                                if (addresses?.isNotEmpty() == true) {
                                    val fullAddress = addresses[0].getAddressLine(0)
                                    locationText = "GPS: $fullAddress"
                                } else {
                                    locationText = "Location found but address not available."
                                }
                            } ?: run {
                                locationText = "Unable to fetch location."
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
            Text("Select Location via GPS")
        }

        Spacer(Modifier.height(8.dp))
        Text(locationText, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                if (!addressError) onNextClicked()
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

@Preview(showBackground = true)
@Composable
fun FillYourDetails2ScreenPreview() {
    FillYourDetails2Screen(navController = rememberNavController(), onNextClicked = {})
}
