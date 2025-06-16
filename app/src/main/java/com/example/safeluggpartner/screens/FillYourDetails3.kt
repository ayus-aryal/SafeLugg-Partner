package com.example.safeluggpartner.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.safeluggpartner.R
import com.example.safeluggpartner.myviewmodels.SharedViewModel
import com.example.safeluggpartner.myviewmodels.StorageDetails


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FillYourDetails3Screen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val focusManager = LocalFocusManager.current

    // --- State variables ---
    var capacity by rememberSaveable { mutableStateOf("") }
    var selectedStorageType by rememberSaveable { mutableStateOf("") }
    val storageTypes = listOf("Lockers", "Open Room", "Racks", "Private Room", "Other")

    var luggageSizes by rememberSaveable { mutableStateOf(setOf<String>()) }
    val sizeOptions = listOf("Cabin Size", "Medium", "Large", "Oversized")

    var hasCCTV by rememberSaveable { mutableStateOf(false) }
    var hasStaff by rememberSaveable { mutableStateOf(false) }
    var hasLocks by rememberSaveable { mutableStateOf(false) }
    var securityNotes by rememberSaveable { mutableStateOf("") }

    var openDays by rememberSaveable { mutableStateOf(setOf<String>()) }
    val allDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    var openingTime by rememberSaveable { mutableStateOf("") }
    var closingTime by rememberSaveable { mutableStateOf("") }
    var is24x7 by rememberSaveable { mutableStateOf(false) }

    // --- Validation Errors ---
    val capacityError = capacity.isBlank()
    val storageTypeError = selectedStorageType.isBlank()
    val luggageSizesError = luggageSizes.isEmpty()
    val securityNotesError = securityNotes.isBlank() && !(hasCCTV || hasStaff || hasLocks) // optional
    val openDaysError = openDays.isEmpty() && !is24x7
    val openingTimeError = openingTime.isBlank() && !is24x7
    val closingTimeError = closingTime.isBlank() && !is24x7

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
                // Header
                Column(horizontalAlignment = Alignment.Start) {
                    Icon(
                        painter = painterResource(R.drawable.logo_safeluggpartner1),
                        contentDescription = "SafeLugg Logo",
                        tint = Color.Black,
                        modifier = Modifier.size(70.dp)
                    )
                    Text(
                        "Add Your Storage Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = customFontFamily
                    )
                    Text(
                        "Provide some quick info about your space.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = customFontFamily,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Capacity Field
                FormField(
                    label = "Capacity (No. of bags you can store) *",
                    value = capacity,
                    onValueChange = { capacity = it.filter { ch -> ch.isDigit() } },
                    error = capacityError,
                    errorMessage = "Capacity is required",
                    keyboardType = KeyboardType.Number,
                    placeholderText = "e.g. 50",
                    fontFamily = customFontFamily
                )

                // Storage Type Dropdown
                DropdownMenuField(
                    label = "Storage Type *",
                    options = storageTypes,
                    selectedOption = selectedStorageType,
                    onOptionSelected = { selectedStorageType = it },
                    fontFamily = customFontFamily
                )
                if (storageTypeError) {
                    Text(
                        "Please select a storage type",
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontFamily = customFontFamily,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Luggage Sizes Checkbox List
                Text(
                    "Luggage Sizes Accepted *",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = customFontFamily
                )
                sizeOptions.forEach { size ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Checkbox(
                            checked = luggageSizes.contains(size),
                            onCheckedChange = {
                                luggageSizes = if (it) luggageSizes + size else luggageSizes - size
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.Black,
                                uncheckedColor = Color.Gray
                            )
                        )
                        Text(size, color = Color.Black, fontFamily = customFontFamily)
                    }
                }
                if (luggageSizesError) {
                    Text(
                        "Select at least one luggage size",
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontFamily = customFontFamily,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Security Measures Section
                Text(
                    "Security Measures",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = customFontFamily
                )

                LabeledSwitch(
                    label = "CCTV Surveillance",
                    checked = hasCCTV,
                    onCheckedChange = { hasCCTV = it },
                    fontFamily = customFontFamily
                )
                LabeledSwitch(
                    label = "Staff Available On-site",
                    checked = hasStaff,
                    onCheckedChange = { hasStaff = it },
                    fontFamily = customFontFamily
                )
                LabeledSwitch(
                    label = "Secure Locking",
                    checked = hasLocks,
                    onCheckedChange = { hasLocks = it },
                    fontFamily = customFontFamily
                )

                OutlinedTextField(
                    value = securityNotes,
                    onValueChange = { securityNotes = it },
                    label = { Text("Additional Security Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Guard on duty from 9 AM to 9 PM") },
                    textStyle = LocalTextStyle.current.copy(fontFamily = customFontFamily)
                )
                if (securityNotesError) {
                    Text(
                        "Please provide security details or enable at least one security measure",
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontFamily = customFontFamily,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Operating Timings Section
                Text(
                    "Operating Timings",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = customFontFamily
                )

                Text(
                    "Open Days *",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontFamily = customFontFamily
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allDays.forEach { day ->
                        FilterChip(
                            selected = openDays.contains(day),
                            onClick = {
                                openDays = if (openDays.contains(day)) openDays - day else openDays + day
                            },
                            label = {
                                Text(day, fontFamily = customFontFamily)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.Black.copy(alpha = 0.1f),
                                labelColor = if (openDays.contains(day)) Color.Black else Color.Gray
                            )
                        )
                    }
                }
                if (openDaysError) {
                    Text(
                        "Select at least one open day or enable 24/7 access",
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontFamily = customFontFamily,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                LabeledSwitch(
                    label = "24/7 Access",
                    checked = is24x7,
                    onCheckedChange = { is24x7 = it },
                    fontFamily = customFontFamily
                )

                if (!is24x7) {
                    OutlinedTextField(
                        value = openingTime,
                        onValueChange = { openingTime = it },
                        label = { Text("Opening Time (e.g., 09:00 AM)") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = openingTimeError,
                        placeholder = { Text("e.g. 09:00 AM") },
                        textStyle = LocalTextStyle.current.copy(fontFamily = customFontFamily)
                    )
                    if (openingTimeError) {
                        Text(
                            "Please enter opening time or enable 24/7 access",
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontFamily = customFontFamily,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    OutlinedTextField(
                        value = closingTime,
                        onValueChange = { closingTime = it },
                        label = { Text("Closing Time (e.g., 09:00 PM)") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = closingTimeError,
                        placeholder = { Text("e.g. 09:00 PM") },
                        textStyle = LocalTextStyle.current.copy(fontFamily = customFontFamily)
                    )
                    if (closingTimeError) {
                        Text(
                            "Please enter closing time or enable 24/7 access",
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontFamily = customFontFamily,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        if (!capacityError && !storageTypeError && !luggageSizesError && !securityNotesError && !openDaysError && !openingTimeError && !closingTimeError) {
                            val storageDetails = StorageDetails(capacity,selectedStorageType, luggageSizes, hasCCTV, hasStaff, hasLocks, securityNotes, openDays, openingTime, closingTime, is24x7 )
                            viewModel.setStorageDetails(storageDetails)
                            navController.navigate("review_screen")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    enabled = !capacityError && !storageTypeError && !luggageSizesError && !securityNotesError && !openDaysError && !openingTimeError && !closingTimeError
                ) {
                    Text(
                        "Next Step — Review Details",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontFamily = customFontFamily
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )                }
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
    placeholderText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    fontFamily: FontFamily,
) {
    Column {
        Text(
            label,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = fontFamily
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            isError = error,
            placeholder = { Text(placeholderText, fontFamily = fontFamily) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            /* colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black,
                textColor = Color.Black,
                errorBorderColor = Color.Red
            ), */
            textStyle = LocalTextStyle.current.copy(fontFamily = fontFamily)
        )
        if (error) {
            Text(
                errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = fontFamily,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    fontFamily: FontFamily
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            label,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontFamily = fontFamily
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontFamily = fontFamily)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontFamily = fontFamily) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LabeledSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    fontFamily: FontFamily
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                uncheckedThumbColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, fontFamily = fontFamily, color = Color.Black)
    }
}

