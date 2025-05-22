package com.example.safeluggpartner.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillYourDetails1Screen(
    onNextClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var businessName by rememberSaveable { mutableStateOf("") }
    var businessType by rememberSaveable { mutableStateOf("") }
    var ownerName by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val businessTypeOptions = listOf("Hotel", "Shop", "Cafe", "Restaurant", "Other")

    // Error state tracking
    val businessNameError = businessName.isBlank()
    val businessTypeError = businessType.isBlank()
    val ownerNameError = ownerName.isBlank()
    val phoneError = phoneNumber.length != 10
    val emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Fill the basic infos",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Let’s get to know your business",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            FieldLabel("Business Name *")
            FormField(
                label = "Business Name",
                value = businessName,
                onValueChange = { businessName = it },
                error = businessNameError,
                errorMessage = "Business name is required"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("Business Type *")
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = businessType,
                    onValueChange = {},
                    readOnly = true,
                    isError = businessTypeError,
                    placeholder = { Text("Select one", color = Color.Black.copy(alpha = 0.6f)) },
                    trailingIcon = {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    },
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    businessTypeOptions.forEach {
                        DropdownMenuItem(
                            text = { Text(it, color = Color.Black) },
                            onClick = {
                                businessType = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            AnimatedVisibility(visible = businessTypeError) {
                Text(
                    "Please select a business type",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("Owner Full Name *")
            FormField(
                label = "Owner Full Name",
                value = ownerName,
                onValueChange = { ownerName = it },
                error = ownerNameError,
                errorMessage = "Owner name is required"
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("Phone Number *")
            FormField(
                label = "Phone Number",
                value = phoneNumber,
                onValueChange = { phoneNumber = it.filter { it.isDigit() }.take(10) },
                error = phoneError,
                errorMessage = "Enter a valid 10-digit phone number",
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            FieldLabel("Email Address *")
            FormField(
                label = "Email Address",
                value = email,
                onValueChange = { email = it },
                error = emailError,
                errorMessage = "Enter a valid email",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    onNextClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !businessNameError && !businessTypeError && !ownerNameError && !phoneError && !emailError,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Next", color = Color.White, fontWeight = FontWeight.SemiBold)
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
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, color = Color.Black) },
            placeholder = { Text(label, color = Color.Black.copy(alpha = 0.6f)) },
            isError = error,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        AnimatedVisibility(visible = error) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun FillYourDetails1ScreenPreview() {
    FillYourDetails1Screen(onNextClicked = {})
}
