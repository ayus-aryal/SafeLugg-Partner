package com.example.safeluggpartner

import PreferenceHelper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.safeluggpartner.myviewmodels.GoogleSignInViewModel
import com.example.safeluggpartner.myviewmodels.GoogleSignInViewModelFactory
import com.example.safeluggpartner.myviewmodels.SharedViewModel
import com.example.safeluggpartner.myviewmodels.SharedViewModelFactory
import com.example.safeluggpartner.network.RetrofitInstance
import com.example.safeluggpartner.screens.*
import com.example.safeluggpartner.ui.theme.SafeLuggPartnerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SafeLuggPartnerTheme {
                SafeLuggPartnerApp()
            }
        }
    }
}

@Composable
fun SafeLuggPartnerApp() {
    val context = LocalContext.current
    val navController = rememberNavController()

    val vendorApi = remember { RetrofitInstance.api }
    val sharedViewModel: SharedViewModel = viewModel(factory = SharedViewModelFactory(vendorApi))
    val googleSignInViewModel: GoogleSignInViewModel = viewModel(factory = GoogleSignInViewModelFactory(vendorApi))

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val isSubmitted = PreferenceHelper.isDetailsSubmitted(context)
        val savedEmail = PreferenceHelper.getVendorEmail(context)

        if (!isSubmitted || savedEmail.isNullOrEmpty()) {
            startDestination = "welcome_screen"
        } else {
            try {
                val response = vendorApi.getVerifcationStatus(savedEmail)
                startDestination = if (response.isSuccessful && response.body() == true) {
                    "vendor_dashboard"
                } else {
                    "verification_pending_screen"
                }
            } catch (e: Exception) {
                startDestination = "welcome_screen"
            }
        }
    }

    if (startDestination != null) {
        NavHost(navController = navController, startDestination = startDestination!!) {
            composable("welcome_screen") {
                WelcomeScreen {
                    googleSignInViewModel.handleGoogleSignIn(
                        context = context,
                        navController = navController
                    )
                }
            }
            composable("fill_your_details1_screen") {
                FillYourDetails1Screen(navController = navController, sharedViewModel)
            }
            composable("fill_your_details2_screen") {
                FillYourDetails2Screen(navController = navController, sharedViewModel)
            }
            composable("fill_your_details3_screen") {
                FillYourDetails3Screen(navController = navController, sharedViewModel)
            }
            composable("fill_your_details4_screen") {
                FillYourDetails4Screen(navController = navController, sharedViewModel)
            }
            composable("fill_your_details5_screen") {
                FillYourDetails5Screen(navController = navController, sharedViewModel)
            }
            composable("review_screen") {
                ReviewScreen(sharedViewModel, navController = navController)
            }
            composable("verification_pending_screen") {
                VerificationPendingScreen(navController = navController)
            }
            composable("vendor_dashboard") {
                VendorDashboard(navController = navController)
            }
        }
    }
}