package com.omnivoiceai.neuromirror.ui.screens.auth.register

import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.omnivoiceai.neuromirror.R
import com.omnivoiceai.neuromirror.ui.navigation.NavigationRoute

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navController: NavController,
) {
    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }
    
    var usernameError by remember { mutableStateOf("") }
    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var repeatPasswordError by remember { mutableStateOf("") }
    
    val registerState by viewModel.registerState.collectAsState()
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

    fun validateForm(): Boolean {
        usernameError = ""
        firstNameError = ""
        lastNameError = ""
        emailError = ""
        passwordError = ""
        repeatPasswordError = ""
        
        if (username.isBlank()) {
            usernameError = context.getString(R.string.please_fill_all_fields)
            return false
        }
        
        if (firstName.isBlank()) {
            firstNameError = context.getString(R.string.please_fill_all_fields)
            return false
        }
        
        if (lastName.isBlank()) {
            lastNameError = context.getString(R.string.please_fill_all_fields)
            return false
        }
        
        if (email.isBlank()) {
            emailError = context.getString(R.string.please_fill_all_fields)
            return false
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = context.getString(R.string.invalid_email)
            return false
        }
        
        if (password.isBlank()) {
            passwordError = context.getString(R.string.please_fill_all_fields)
            return false
        }
        
        if (password.length < 6) {
            passwordError = context.getString(R.string.password_min_length)
            return false
        }
        
        if (repeatPassword.isBlank()) {
            repeatPasswordError = context.getString(R.string.please_fill_all_fields)
            return false
        }
        
        if (password != repeatPassword) {
            repeatPasswordError = context.getString(R.string.passwords_do_not_match)
            return false
        }
        
        return true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.register),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { 
                        username = it
                        usernameError = ""
                    },
                    label = { Text(stringResource(R.string.username)) },
                    leadingIcon = { 
                        Icon(Icons.Default.Person, contentDescription = null) 
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = usernameError.isNotEmpty(),
                    supportingText = if (usernameError.isNotEmpty()) {
                        { Text(usernameError, color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { 
                            firstName = it
                            firstNameError = ""
                        },
                        label = { Text(stringResource(R.string.first_name)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        isError = firstNameError.isNotEmpty(),
                        supportingText = if (firstNameError.isNotEmpty()) {
                            { Text(firstNameError, color = MaterialTheme.colorScheme.error) }
                        } else null
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { 
                            lastName = it
                            lastNameError = ""
                        },
                        label = { Text(stringResource(R.string.last_name)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        isError = lastNameError.isNotEmpty(),
                        supportingText = if (lastNameError.isNotEmpty()) {
                            { Text(lastNameError, color = MaterialTheme.colorScheme.error) }
                        } else null
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        emailError = ""
                    },
                    label = { Text(stringResource(R.string.email)) },
                    leadingIcon = { 
                        Icon(Icons.Default.Email, contentDescription = null) 
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError.isNotEmpty(),
                    supportingText = if (emailError.isNotEmpty()) {
                        { Text(emailError, color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        passwordError = ""
                    },
                    label = { Text(stringResource(R.string.password)) },
                    leadingIcon = { 
                        Icon(Icons.Default.Lock, contentDescription = null) 
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordError.isNotEmpty(),
                    supportingText = if (passwordError.isNotEmpty()) {
                        { Text(passwordError, color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                OutlinedTextField(
                    value = repeatPassword,
                    onValueChange = { 
                        repeatPassword = it
                        repeatPasswordError = ""
                    },
                    label = { Text(stringResource(R.string.repeat_password)) },
                    leadingIcon = { 
                        Icon(Icons.Default.Lock, contentDescription = null) 
                    },
                    trailingIcon = {
                        IconButton(onClick = { repeatPasswordVisible = !repeatPasswordVisible }) {
                            Icon(
                                imageVector = if (repeatPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (repeatPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = repeatPasswordError.isNotEmpty(),
                    supportingText = if (repeatPasswordError.isNotEmpty()) {
                        { Text(repeatPasswordError, color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { 
                        if (validateForm()) {
                            viewModel.signUpWithEmail(email, password, username, firstName, lastName)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = registerState !is RegisterState.Loading
                ) { 
                    if (registerState is RegisterState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(stringResource(R.string.register))
                    }
                }

                when (val state = registerState) {
                    is RegisterState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                    is RegisterState.Success -> {
                        LaunchedEffect(Unit) {
                            navController.navigate(NavigationRoute.HomeScreen) {
                                popUpTo("register") { inclusive = true }
                            }
                        }
                    }
                    else -> { /* Idle or Loading */ }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Divider(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.or),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Divider(modifier = Modifier.weight(1f))
                }

                // Google Sign In Button
                OutlinedButton(
                    onClick = {
                        val client = viewModel.getGoogleSignInRequest()
                        launcher.launch(client.signInIntent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = registerState !is RegisterState.Loading
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape),
                            color = Color.Transparent
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "Google",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.login_with_google))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.already_have_account),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text(
                            text = stringResource(R.string.login),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}