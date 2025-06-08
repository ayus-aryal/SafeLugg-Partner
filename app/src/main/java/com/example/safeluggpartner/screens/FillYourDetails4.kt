package com.example.safeluggpartner.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.safeluggpartner.R


@Composable
fun FillYourDetails4Screen(navController: NavController) {
    var pricePerBag by rememberSaveable { mutableStateOf("") }
    var priceError by rememberSaveable { mutableStateOf(false) }
    var note by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Icon(
                    painter = painterResource(R.drawable.logo_safeluggpartner1),
                    contentDescription = "SafeLugg Logo",
                    tint = Color.Black,
                    modifier = Modifier.size(70.dp)
                )

                Text(
                    text = "Pricing & Bank Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontFamily = customFontFamily,
                )

                Text(
                    "Set your price per bag and let us handle the payouts securely.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontFamily = customFontFamily,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(Modifier.width(8.dp))

                // --- Price per Bag Field using FormField ---
                FormField1(
                    label = "Price per bag (in ₹) *",
                    value = pricePerBag,
                    onValueChange = {
                        pricePerBag = it.filter { ch -> ch.isDigit() }.take(4)
                        priceError = false
                    },
                    error = priceError,
                    errorMessage = "Price per bag is required",
                    keyboardType = KeyboardType.Number,
                    placeholderText = "Enter price per bag (e.g. 100)"
                )

                // --- Optional Info Field ---
                Text(
                    text = "Optional note (e.g., extra charges)",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 3
                )

                // --- Manual Bank Info Note ---
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Our team will manually collect your bank account details for payouts.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = "Alternatively, you can send your account details to:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Text(
                    text = "safelugg@gmail.com",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                val focusManager = LocalFocusManager.current

                // --- Submit Button ---
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        if (pricePerBag.isBlank() || pricePerBag.toIntOrNull() == null || pricePerBag.toInt() <= 0) {
                            priceError = true
                        } else {
                            // Navigate to next screen
                            navController.navigate("next_screen_route")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    enabled = pricePerBag.isNotBlank() && pricePerBag.toIntOrNull() != null && pricePerBag.toInt() > 0
                ) {
                    Text(
                        "Next - Add Photos of Your Space",
                        color = Color.White,
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Medium
                    )
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
fun FormField1(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean,
    errorMessage: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    placeholderText: String = label
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Black,
            fontFamily = customFontFamily,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholderText,
                    color = Color.Gray,
                    fontFamily = customFontFamily
                )
            },
            isError = error,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        if (error) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FillYourDetails4ScreenPreview() {
    FillYourDetails4Screen(navController = rememberNavController())
}
