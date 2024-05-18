package com.example.calendarbyourselvesdacs3.presentation.sign_in

import android.annotation.SuppressLint
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.navigation.NavController
import com.example.calendarbyourselvesdacs3.presentation.navigation.Screen
import com.example.calendarbyourselvesdacs3.presentation.sign_in.data.SignInResult
import com.example.calendarbyourselvesdacs3.presentation.sign_in.data.UserData
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.util.concurrent.CancellationException


const val WEB_CLIENT_ID = "452209938277-f6alp08v50noeom4bbe7632d8uhga986.apps.googleusercontent.com"

class GoogleAuthFireBase(
    private val context: Context,
    private val navController: NavController,
    private val auth: FirebaseAuth
) {

    private val credentialManager = CredentialManager.create(context)


    @SuppressLint("SuspiciousIndentation")
    suspend fun signIn(): SignInResult? {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        var signInResult: SignInResult? = null

            try {
                val result = credentialManager.getCredential(context = context, request = request)
                val credential = result.credential
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            navController.popBackStack()
                            navController.navigate(Screen.HomeScreen.name)

                            auth.currentUser?.let {
                                signInResult = SignInResult(
                                    data = UserData(it.uid, it.displayName, it.photoUrl?.toString()),
                                    errorMessage = null)
                            }
                        }
                    }
                return signInResult
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                return SignInResult(data = null, errorMessage = e.message)
            }
    }

    suspend fun signOut(){
        auth.signOut()
            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )

        navController.popBackStack()
        navController.navigate(Screen.SignInScreen.name)
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilPictureUrl = photoUrl?.toString()
        )
    }
}