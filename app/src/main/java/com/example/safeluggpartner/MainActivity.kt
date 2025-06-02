package com.example.safeluggpartner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.safeluggpartner.myviewmodels.GoogleSignInViewModel
import com.example.safeluggpartner.screens.FillYourDetails1Screen
import com.example.safeluggpartner.screens.FillYourDetails2Screen
import com.example.safeluggpartner.screens.FillYourDetails3Screen
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
        val navController = rememberNavController()
        val googleSignInViewModel = GoogleSignInViewModel()

        SafeLuggPartnerTheme {
            NavHost(navController = navController, startDestination = "fill_your_details3_screen") {
                composable(route = "welcome_screen") {
                    WelcomeScreen {
                        googleSignInViewModel.handleGoogleSignIn(navController.context, navController)
                    }
                }
                composable(route = "fill_your_details1_screen"){
                    FillYourDetails1Screen(navController = navController)
                }
                composable(route = "fill_your_details2_screen"){
                    FillYourDetails2Screen(
                        navController = navController
                    )
                }
                composable(route = "fill_your_details3_screen"){
                    FillYourDetails3Screen(
                        navController = navController
                    )
                }
                
            }
        }
    }




}

