package com.example.safeluggpartner.myviewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.safeluggpartner.R
import com.example.safeluggpartner.network.VendorApiService
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class GoogleSignInViewModel(private val vendorApiService: VendorApiService) : ViewModel() {

    fun handleGoogleSignIn(context: Context, navController: NavController) {
        viewModelScope.launch {
            googleSignIn(context).collect { result ->
                result.fold(
                    onSuccess = { authResult ->
                        val user = authResult.user
                        val email = user?.email
                        if (!email.isNullOrEmpty()) {
                            PreferenceHelper.saveVendorEmail(context, email)
                            checkWithBackendAndNavigate(context, email, navController)
                        }
                    },
                    onFailure = {
                        Toast.makeText(context, "Sign-in Failed!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private suspend fun googleSignIn(context: Context): Flow<Result<AuthResult>> = callbackFlow {
        try {
            val credentialManager = CredentialManager.create(context)
            val hashedNonce = UUID.randomUUID().toString().sha256()
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .setNonce(hashedNonce)
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseAuth = FirebaseAuth.getInstance()
                val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                trySend(Result.success(authResult))
            } else {
                throw IllegalArgumentException("Invalid Google Credential")
            }
        } catch (e: Exception) {
            trySend(Result.failure(e))
        }
        awaitClose()
    }

    private fun checkWithBackendAndNavigate(context: Context, email: String, navController: NavController) {
        viewModelScope.launch {
            try {
                val existsResponse = vendorApiService.checkVendorExists(email)
                if (existsResponse.isSuccessful && existsResponse.body() == true) {
                    PreferenceHelper.setDetailsSubmitted(context, true)
                    val verifyResponse = vendorApiService.getVerifcationStatus(email)
                    if (verifyResponse.isSuccessful && verifyResponse.body() == true) {
                        navController.navigate("vendor_dashboard") {
                            popUpTo("welcome_screen") { inclusive = true }
                        }
                    } else {
                        navController.navigate("verification_pending_screen") {
                            popUpTo("welcome_screen") { inclusive = true }
                        }
                    }
                } else {
                    PreferenceHelper.setDetailsSubmitted(context, false)
                    navController.navigate("fill_your_details1_screen") {
                        popUpTo("welcome_screen") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                PreferenceHelper.setDetailsSubmitted(context, false)
                navController.navigate("fill_your_details1_screen") {
                    popUpTo("welcome_screen") { inclusive = true }
                }
            }
        }
    }

    private fun String.sha256(): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(this.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
