package com.omnivoiceai.neuromirror.data.repositories

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
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
    context: Context
) {
    private val _userFlow = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val userFlow: StateFlow<FirebaseUser?> = _userFlow.asStateFlow()

    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    private val signInRequest: BeginSignInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false) // Mostra anche account non salvati
                .build()
        )
        .setAutoSelectEnabled(false)
        .build()

    private val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    )

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _userFlow.value = firebaseAuth.currentUser
        }
    }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser> =
        suspendCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    it.user?.let { user ->
                        cont.resume(Result.success(user))
                    } ?: cont.resume(Result.failure(IllegalStateException("User is null after sign-up")))
                }
                .addOnFailureListener {
                    cont.resume(Result.failure(it))
                }
        }

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> =
        suspendCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    it.user?.let { user ->
                        cont.resume(Result.success(user))
                    } ?: cont.resume(Result.failure(IllegalStateException("User is null after sign-in")))
                }
                .addOnFailureListener {
                    cont.resume(Result.failure(it))
                }
        }

    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> =
        suspendCoroutine { cont ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    val user = auth.currentUser
                    if (user != null) {
                        cont.resume(Result.success(user))
                    } else {
                        cont.resume(Result.failure(IllegalStateException("User is null after Google sign-in")))
                    }
                }
                .addOnFailureListener {
                    cont.resume(Result.failure(it))
                }
        }

    fun getOneTapClient(): SignInClient = oneTapClient

    fun getSignInRequest(): BeginSignInRequest = signInRequest

    fun getGoogleSignInIntent(): Intent = googleSignInClient.signInIntent

    fun signOut() {
        auth.signOut()
        oneTapClient.signOut()
        googleSignInClient.signOut()
    }

    fun isSignedIn(): Boolean = auth.currentUser != null
}
