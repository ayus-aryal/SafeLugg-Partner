package com.example.safeluggpartner.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.safeluggpartner.R

@Composable
fun FillYourDetails5Screen(navController: NavController) {
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var previewImage by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        selectedImages = selectedImages + uris.filter { it !in selectedImages }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.logo_safeluggpartner1),
                    contentDescription = "SafeLugg Logo",
                    tint = Color.Black,
                    modifier = Modifier.size(70.dp)
                )
                Text(
                    text = "Add Photos of Your Space",
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

                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RectangleShape
                ) {
                    Text(
                        "Select Photos from Gallery",
                        color = Color.White,
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }

                selectedImages.forEach { uri ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(180.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { previewImage = uri },
                            contentScale = ContentScale.Crop
                        )

                        IconButton(
                            onClick = {
                                selectedImages = selectedImages.toMutableList().also { it.remove(uri) }
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(28.dp)
                                .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.google), // replace with your close icon
                                contentDescription = "Remove image",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    enabled = selectedImages.isNotEmpty(),
                ) {
                    Text(
                        text = "Submit",
                        color = Color.White,
                        fontFamily = customFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    // Full-Screen Image Preview Dialog
    if (previewImage != null) {
        AlertDialog(
            onDismissRequest = { previewImage = null },
            confirmButton = {},
            modifier = Modifier.fillMaxSize(),
            text = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = rememberAsyncImagePainter(previewImage),
                        contentDescription = "Preview",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                    IconButton(
                        onClick = { previewImage = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.google), // replace with your close icon
                            contentDescription = "Close Preview",
                            tint = Color.White
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FillYourDetails5ScreenPreview() {
    FillYourDetails5Screen(rememberNavController())
}
