package com.example.safeluggpartner.screens

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.safeluggpartner.myviewmodels.SharedViewModel
import com.example.safeluggpartner.network.RetrofitInstance
import com.example.safeluggpartner.util.getFileFromUri
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun ReviewScreen(viewModel: SharedViewModel, navController: NavController) {
    val personalDetails = viewModel.personalDetails.value
    val locationDetails = viewModel.locationDetails.value
    val storageDetails = viewModel.storageDetails.value
    val pricingDetails = viewModel.pricingDetails.value
    val selectedImageUris = viewModel.selectedImageUris.value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val finalRequest = viewModel.getFinalSubmissionRequest()
    val gson = remember { Gson() }



    if (personalDetails != null && locationDetails != null && pricingDetails != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Review Your Personal Info",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = customFontFamily
                    )

                    InfoTextRow("Business Name", personalDetails.businessName)
                    InfoTextRow("Owner Name", personalDetails.ownerName)
                    InfoTextRow("Phone Number", personalDetails.phoneNumber)
                    InfoTextRow("Email", personalDetails.email)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(16.dp)

            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Review Your Location Info",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = customFontFamily
                    )

                    InfoTextRow("Country", locationDetails.country)
                    InfoTextRow("State", locationDetails.state)
                    InfoTextRow("City", locationDetails.city)
                    InfoTextRow("Postal Code", locationDetails.postalCode)
                    InfoTextRow("Street Address", locationDetails.streetAddress)
                    InfoTextRow("Landmark", locationDetails.landmark)
                    InfoTextRow("Location Text", locationDetails.locationText)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Storage & Availability Info",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = customFontFamily
                    )

                    storageDetails?.let {
                        InfoTextRow("Capacity", it.capacity)
                        InfoTextRow("Storage Type", it.storageTypes)
                        InfoTextRow("Luggage Sizes", it.luggageSizes.joinToString())
                        InfoTextRow("Has CCTV", if (it.hasCCTV) "Yes" else "No")
                        InfoTextRow("Has Staff", if (it.hasStaff) "Yes" else "No")
                        InfoTextRow("Has Locks", if (it.hasLocks) "Yes" else "No")
                        InfoTextRow("Security Notes", it.securityNotes.ifBlank { "None" })
                        InfoTextRow("Operating Days", it.openDays.joinToString())
                        InfoTextRow("Opening Time", if (it.is24x7) "Open 24x7" else it.openingTime)
                        if (!it.is24x7) {
                            InfoTextRow("Closing Time", it.closingTime)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(16.dp)
            ){
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Text(
                        text = "Pricing Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = customFontFamily
                    )
                    pricingDetails.let{
                        InfoTextRow("Price Per Bag", it.pricePerBag)
                        InfoTextRow("Additional Note", it.note)
                    }
                }
            }



            if (selectedImageUris.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Your Storage Photos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontFamily = customFontFamily
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            selectedImageUris.forEach { uri ->
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    androidx.compose.foundation.Image(
                                        painter = rememberAsyncImagePainter(uri),
                                        contentDescription = null,
                                        modifier = Modifier.size(120.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }


            Button(
                onClick = {
                    scope.launch {
                        try {
                            val json = gson.toJson(finalRequest)
                            val dataPart = json.toRequestBody("application/json".toMediaTypeOrNull())

                            val imageParts = selectedImageUris.map { uri ->
                                val file = getFileFromUri(context, uri)
                                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                                MultipartBody.Part.createFormData("images", file.name, reqFile)
                            }

                            val response = RetrofitInstance.api.submitPartnerForm(dataPart, imageParts)

                            if (response.isSuccessful) {
                                Toast.makeText(context, "Submitted successfully!", Toast.LENGTH_LONG).show()
                                navController.navigate("verification_pending_screen")
                            } else {
                                Toast.makeText(context, "Submission failed", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Submit and Continue",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontFamily = customFontFamily
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No details found.",
                fontSize = 18.sp,
                color = Color.Red,
                fontFamily = customFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun InfoTextRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Black,
            fontFamily = customFontFamily,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.DarkGray,
            fontFamily = customFontFamily,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

