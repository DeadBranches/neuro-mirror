package com.omnivoiceai.neuromirror.ui.screens.auth.login

import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import com.omnivoiceai.neuromirror.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    data object Idle    : LoginState()
    data object Loading : LoginState()
    data class Success(val user: FirebaseUser) : LoginState()
    data class Error  (val message: String)  : LoginState()
}

class LoginViewModel(
    private val authRepository: AuthRepository
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

    fun getGoogleSignInRequest(): GoogleSignInClient =
        authRepository.getGoogleSignInClient()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            authRepository.signInWithGoogle(idToken).fold(
                onSuccess = { user ->
                    _loginState.value = LoginState.Success(user)
                },
                onFailure = { exc ->
                    _loginState.value = LoginState.Error(exc.localizedMessage ?: "Login Google fallito")
                }
            )
        }
    }

    fun signOut() {
        authRepository.signOut()
        _loginState.value = LoginState.Idle
    }
}
