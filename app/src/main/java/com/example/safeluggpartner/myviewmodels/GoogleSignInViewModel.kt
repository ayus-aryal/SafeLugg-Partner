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

class GoogleSignInViewModel : ViewModel() {

    fun handleGoogleSignIn(context: Context, navController: NavController) {
        viewModelScope.launch {
            googleSignIn(context).collect { result ->
                result.fold(
                    onSuccess = { authResult ->
                        val user = authResult.user
                        if (user != null) {
                            handleSignInResult(user, navController)
                        } else {
                            Toast.makeText(context, "User not found!", Toast.LENGTH_LONG).show()
                        }
                    },
                    onFailure = { e ->
                        Toast.makeText(
                            context,
                            "Something went wrong: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("Issue", "handleGoogleSignIn: ${e.message}")
                    }
                )
            }
        }
    }

    private suspend fun googleSignIn(context: Context): Flow<Result<AuthResult>> {
        val firebaseAuth = FirebaseAuth.getInstance()

        return callbackFlow {
            try {
                val credentialManager: CredentialManager = CredentialManager.create(context)

                val ranNonce: String = UUID.randomUUID().toString()
                val bytes: ByteArray = ranNonce.toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                val digest: ByteArray = md.digest(bytes)
                val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }

                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setNonce(hashedNonce)
                    .setAutoSelectEnabled(true)
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                    trySend(Result.success(authResult))
                } else {
                    throw RuntimeException("Received an invalid credential type.")
                }

            } catch (e: GetCredentialCancellationException) {
                trySend(Result.failure(Exception("Sign-in was canceled.")))
            } catch (e: Exception) {
                trySend(Result.failure(e))
            }

            awaitClose { }
        }
    }

    private fun handleSignInResult(user: FirebaseUser, navController: NavController) {
        val firestore = FirebaseFirestore.getInstance()
        val userId = user.uid

        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("Auth", "User already exists in Firestore. Redirecting to Home.")
                    navController.navigate("fill_your_details2_screen") {
                        popUpTo("splash_screen") { inclusive = true }
                    }
                } else {
                    Log.d("Auth", "User does not exist in Firestore. Redirecting to Fill Details.")
                    val newUser = hashMapOf(
                        "email" to user.email,
                        "firstName" to "",
                        "lastName" to "",
                        "phoneNumber" to ""
                    )
                    firestore.collection("users").document(userId).set(newUser)
                        .addOnSuccessListener {
                            Log.d("Auth", "New user profile created in Firestore.")
                            navController.navigate("fill_your_details2_screen") {
                                popUpTo("splash_screen") { inclusive = true }
                            }
                        }
                        .addOnFailureListener {
                            Log.e("Auth", "Firestore write failed: ${it.message}")
                        }
                }
            }
            .addOnFailureListener {
                Log.e("Auth", "Firestore read failed: ${it.message}")
            }
    }
}
