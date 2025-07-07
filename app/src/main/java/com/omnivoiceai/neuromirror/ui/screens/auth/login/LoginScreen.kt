package com.omnivoiceai.neuromirror.ui.screens.auth.login

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GMobiledata
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
                ?: throw IllegalStateException("Account Google nullo")
            val idToken = account.idToken
                ?: throw IllegalStateException("ID token non presente")
            viewModel.signInWithGoogle(idToken)
        } catch (e: Exception) {
            Toast
                .makeText(context, "Google sign-in fallito: ${e.localizedMessage}", Toast.LENGTH_LONG)
                .show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))
        when (loginState) {
            LoginState.Loading -> CircularProgressIndicator()
            is LoginState.Error -> Text(
                text = (loginState as LoginState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
            is LoginState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate(NavigationRoute.HomeScreen) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            else -> { /* Idle */ }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.signInWithEmail(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Login") }

        Spacer(Modifier.height(16.dp))
        Text("Or", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .clickable {
                    val client = viewModel.getGoogleSignInRequest()
                    launcher.launch(client.signInIntent)
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.GMobiledata, contentDescription = "Google")
            Spacer(Modifier.width(8.dp))
            Text(
                "Login with Google",
                textDecoration = TextDecoration.Underline,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(Modifier.height(24.dp))
        TextButton(onClick = { navController.navigate(NavigationRoute.RegisterScreen) }) {
            Text("Not have an account? Register")
        }
    }
}
