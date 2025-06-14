package com.example.safeluggpartner.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safeluggpartner.myviewmodels.SharedViewModel

@Composable
fun ReviewScreen(viewModel: SharedViewModel) {
    val personalDetails = viewModel.personalDetails.value
    val locationDetails = viewModel.locationDetails.value

    if (personalDetails != null && locationDetails != null) {
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

            Spacer(modifier = Modifier.height(20.dp))

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

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    // Proceed to next step or final submit
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
