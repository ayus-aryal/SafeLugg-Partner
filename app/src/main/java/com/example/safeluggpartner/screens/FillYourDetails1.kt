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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.safeluggpartner.R
import com.example.safeluggpartner.myviewmodels.SharedViewModel
import com.example.safeluggpartner.myviewmodels.PersonalDetails



val customFontFamily = FontFamily(Font(R.font.inter))



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillYourDetails1Screen(navController: NavController,
                            viewModel: SharedViewModel) {
    val focusManager = LocalFocusManager.current

    var businessName by rememberSaveable { mutableStateOf("") }
    var ownerName by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    val businessNameError = businessName.isBlank()
    val ownerNameError = ownerName.isBlank()
    val phoneError = phoneNumber.length != 10
    val emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .padding(top =25.dp),
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
                // Logo & Header
                Column(horizontalAlignment = Alignment.Start) {
                    Icon(
                        painter = painterResource(R.drawable.logo_safeluggpartner1),
                        contentDescription = "SafeLugg Logo",
                        tint = Color.Black,
                        modifier = Modifier.size(70.dp)
                    )
                    Text(
                        "Become a SafeLugg Partner",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = customFontFamily

                    )
                    Text(
                        "Turn your idle space into income.\nNo investment needed.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = customFontFamily,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Form Fields
                FormField("Business Name *", businessName, { businessName = it }, businessNameError, "Business name is required", placeholderText = "Enter your business name")
                FormField("Owner's Full Name *", ownerName, { ownerName = it }, ownerNameError, "Owner name is required", placeholderText = "Enter your full name")
                FormField(
                    "Mobile Number *",
                    phoneNumber,
                    { phoneNumber = it.filter { ch -> ch.isDigit() }.take(10) },
                    phoneError,
                    "Enter a valid 10-digit number",
                    KeyboardType.Phone,
                    placeholderText = "Enter your phone number"
                )
                FormField(
                    "Email Address *",
                    email,
                    { email = it },
                    emailError,
                    "Enter a valid email",
                    KeyboardType.Email,
                    placeholderText = "Enter your email"
                )

                // Button
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        if (!businessNameError && !ownerNameError && !phoneError && !emailError) {
                            val personalDetails = PersonalDetails(businessName, ownerName, phoneNumber, email)
                            viewModel.setUserDetails(personalDetails)
                            navController.navigate("fill_your_details2_screen")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    enabled = !businessNameError && !ownerNameError && !phoneError && !emailError
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Next Step — Tell Us About Your Space",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontFamily = customFontFamily

                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean,
    errorMessage: String,
    keyboardType: KeyboardType = KeyboardType.Text ,
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
            placeholder = { Text(text = placeholderText, color = Color.Gray, fontFamily = customFontFamily
            ) },
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

