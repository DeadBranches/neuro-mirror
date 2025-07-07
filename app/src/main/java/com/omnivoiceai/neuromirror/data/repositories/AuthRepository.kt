package com.omnivoiceai.neuromirror.data.repositories

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.omnivoiceai.neuromirror.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository(
    private val auth: FirebaseAuth,
    private val context: Context
) {
    private val _userFlow = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val userFlow: StateFlow<FirebaseUser?> = _userFlow.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _userFlow.value = firebaseAuth.currentUser
        }
    }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser> =
        suspendCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { cont.resume(Result.success(it.user!!)) }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> =
        suspendCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { cont.resume(Result.success(it.user!!)) }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }

    fun getGoogleSignInClient(): GoogleSignInClient {
        val serverClientId = context.getString(R.string.default_web_client_id)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)    // ← qui chiedi l’idToken
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }


    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> =
        suspendCoroutine { cont ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnSuccessListener { cont.resume(Result.success(auth.currentUser!!)) }
                .addOnFailureListener { cont.resume(Result.failure(it)) }
        }

    fun signOut() {
        auth.signOut()
        getGoogleSignInClient().signOut()
    }
}
