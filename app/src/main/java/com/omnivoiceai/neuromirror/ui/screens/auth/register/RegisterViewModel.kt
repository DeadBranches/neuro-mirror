package com.omnivoiceai.neuromirror.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseUser
import com.omnivoiceai.neuromirror.data.repositories.AuthRepository
import com.omnivoiceai.neuromirror.data.repositories.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data class Success(val user: FirebaseUser) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun signUpWithEmail(email: String, password: String, username: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            authRepository.signUp(email, password).fold(
                onSuccess = { user ->
                    saveProfileData(username, firstName, lastName)
                    _registerState.value = RegisterState.Success(user)
                },
                onFailure = { exc ->
                    _registerState.value = RegisterState.Error(exc.localizedMessage ?: "Registrazione fallita")
                }
            )
        }
    }

    private suspend fun saveProfileData(username: String, firstName: String, lastName: String) {
        try {
            profileRepository.setUsername(username)
            profileRepository.setFirstName(firstName)
            profileRepository.setLastName(lastName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getOneTapClient(): SignInClient = authRepository.getOneTapClient()

    fun getSignInRequest(): BeginSignInRequest = authRepository.getSignInRequest()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            authRepository.signInWithGoogle(idToken).fold(
                onSuccess = { user ->
                    saveGoogleProfileData(user)
                    _registerState.value = RegisterState.Success(user)
                },
                onFailure = { exc ->
                    _registerState.value = RegisterState.Error(exc.localizedMessage ?: "Login Google fallito")
                }
            )
        }
    }

    private suspend fun saveGoogleProfileData(user: FirebaseUser) {
        try {
            val displayName = user.displayName ?: ""
            val email = user.email ?: ""

            val username = displayName.ifEmpty { email.substringBefore("@") }

            val nameParts = displayName.split(" ", limit = 2)
            val firstName = nameParts.getOrNull(0) ?: ""
            val lastName = nameParts.getOrNull(1) ?: ""

            if (username.isNotEmpty()) {
                profileRepository.setUsername(username)
            }
            if (firstName.isNotEmpty()) {
                profileRepository.setFirstName(firstName)
            }
            if (lastName.isNotEmpty()) {
                profileRepository.setLastName(lastName)
            }

            user.photoUrl?.toString()?.let { imageUrl ->
                profileRepository.setImageUrl(imageUrl)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}