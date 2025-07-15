package com.example.safeluggpartner

import PreferenceHelper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.safeluggpartner.myviewmodels.GoogleSignInViewModel
import com.example.safeluggpartner.myviewmodels.SharedViewModel
import com.example.safeluggpartner.myviewmodels.SharedViewModelFactory
import com.example.safeluggpartner.network.RetrofitInstance
import com.example.safeluggpartner.screens.FillYourDetails1Screen
import com.example.safeluggpartner.screens.FillYourDetails2Screen
import com.example.safeluggpartner.screens.FillYourDetails3Screen
import com.example.safeluggpartner.screens.FillYourDetails4Screen
import com.example.safeluggpartner.screens.FillYourDetails5Screen
import com.example.safeluggpartner.screens.ReviewScreen
import com.example.safeluggpartner.screens.VendorDashboard
import com.example.safeluggpartner.screens.VerificationPendingScreen
import com.example.safeluggpartner.screens.WelcomeScreen
import com.example.safeluggpartner.ui.theme.SafeLuggPartnerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SafeLuggPartnerTheme {
                SafeLuggPartner()
            }
        }
    }


    @Composable
    fun SafeLuggPartner() {
        val context = LocalContext.current

        val navController = rememberNavController()
        val googleSignInViewModel = GoogleSignInViewModel()

        val vendorApi = remember { RetrofitInstance.api }
        val factory = remember { SharedViewModelFactory(vendorApi) }
        val sharedViewModel: SharedViewModel = viewModel(factory = factory)


        val isSubmitted = remember { PreferenceHelper.isDetailsSubmitted(context) }
        var startDestination by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(isSubmitted) {
            if (!isSubmitted) {
                startDestination = "fill_your_details1_screen"
            } else {
                val savedEmail = PreferenceHelper.getVendorEmail(context)
                savedEmail?.isEmpty()?.let {
                    if (!it) {
                        try {
                            val response = vendorApi.getVerifcationStatus(savedEmail)
                            startDestination =
                                if (response.isSuccessful && response.body() == true) {
                                    "vendor_dashboard"
                                } else {
                                    "verification_pending_screen"
                                }
                        } catch (e: Exception) {
                            startDestination = "fill_your_details1_screen"
                        }
                    }
                }
            }
        }




        SafeLuggPartnerTheme {
            if (startDestination != null) {
                NavHost(navController = navController, startDestination = startDestination!!) {
                    composable("welcome_screen") {
                        WelcomeScreen {
                            googleSignInViewModel.handleGoogleSignIn(
                                navController.context,
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


    }
}

