package com.omnivoiceai.neuromirror.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseUser
import com.omnivoiceai.neuromirror.data.repositories.AuthRepository
import com.omnivoiceai.neuromirror.data.repositories.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class LoginState {
    data object Idle    : LoginState()
    data object Loading : LoginState()
    data class Success(val user: FirebaseUser) : LoginState()
    data class Error  (val message: String)  : LoginState()
}

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    val currentUser: StateFlow<FirebaseUser?> = authRepository.userFlow

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            authRepository.signIn(email, password).fold(
                onSuccess = { user ->
                    _loginState.value = LoginState.Success(user)
                },
                onFailure = { exc ->
                    _loginState.value = LoginState.Error(exc.localizedMessage ?: "Login fallito")
                }
            )
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            authRepository.signUp(email, password).fold(
                onSuccess = { user ->
                    _loginState.value = LoginState.Success(user)
                },
                onFailure = { exc ->
                    _loginState.value = LoginState.Error(exc.localizedMessage ?: "Registrazione fallita")
                }
            )
        }
    }

    fun getOneTapClient(): SignInClient = authRepository.getOneTapClient()
    fun getSignInRequest() = authRepository.getSignInRequest()

    fun getGoogleSignInIntent() = authRepository.getGoogleSignInIntent()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            authRepository.signInWithGoogle(idToken).fold(
                onSuccess = { user ->
                    updateGoogleProfileData(user)
                    _loginState.value = LoginState.Success(user)
                },
                onFailure = { exc ->
                    _loginState.value = LoginState.Error(exc.localizedMessage ?: "Login Google fallito")
                }
            )
        }
    }

    private suspend fun updateGoogleProfileData(user: FirebaseUser) {
        try {
            val displayName = user.displayName ?: ""
            val email = user.email ?: ""

            if (displayName.isNotEmpty()) {
                val nameParts = displayName.split(" ", limit = 2)
                val firstName = nameParts.getOrNull(0) ?: ""
                val lastName = nameParts.getOrNull(1) ?: ""

                val currentUsername = profileRepository.username.first()
                if (currentUsername.isEmpty()) {
                    profileRepository.setUsername(displayName)
                }

                val currentFirstName = profileRepository.firstName.first()
                val currentLastName = profileRepository.lastName.first()

                if (currentFirstName.isEmpty() && firstName.isNotEmpty()) {
                    profileRepository.setFirstName(firstName)
                }
                if (currentLastName.isEmpty() && lastName.isNotEmpty()) {
                    profileRepository.setLastName(lastName)
                }
            } else if (email.isNotEmpty()) {
                val currentUsername = profileRepository.username.first()
                if (currentUsername.isEmpty()) {
                    profileRepository.setUsername(email.substringBefore("@"))
                }
            }

            user.photoUrl?.toString()?.let { imageUrl ->
                val currentImageUrl = profileRepository.imageUrl.first()
                if (currentImageUrl.isEmpty()) {
                    profileRepository.setImageUrl(imageUrl)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun signOut() {
        authRepository.signOut()
        _loginState.value = LoginState.Idle
    }
}
