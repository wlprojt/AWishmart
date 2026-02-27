package com.example.wishmart.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wishmart.R
import com.example.wishmart.auth.AuthResult
import com.example.wishmart.viewmodel.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignupScreen(navController: NavController, viewModel: MainViewModel) {

    val context = LocalContext.current
    val state = viewModel.state
    var googleLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    /* ---------------- GOOGLE CONFIG ---------------- */

    val googleOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("144637133530-s3k05e25lkl88md8k3hnemfamm5fe0to.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }

    val googleClient = remember {
        GoogleSignIn.getClient(context, googleOptions)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken

            if (idToken != null) {
                viewModel.onEvent(AuthUiEvent.GoogleSignIn(idToken))
            } else {
                Toast.makeText(context, "Failed to get ID Token", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            googleLoading = false
            Toast.makeText(context, "Google Sign Up Failed", Toast.LENGTH_SHORT).show()
        }
    }

    /* ---------------- AUTH RESULT LISTENER ---------------- */

    LaunchedEffect(Unit) {
        viewModel.authResults.collectLatest { result ->

            googleLoading = false

            when (result) {

                is AuthResult.OtpSent -> {
                    navController.navigate("otp") {
                        popUpTo("signup") { inclusive = true }
                    }
                }

                is AuthResult.GoogleSuccess -> {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }

                is AuthResult.Unauthorized -> {
                    Toast.makeText(context, "Signup failed", Toast.LENGTH_LONG).show()
                }

                is AuthResult.UnknownError -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
                }

                else -> Unit
            }
        }
    }


    // Main UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // 👈 Makes page scrollable
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Title
                    Text(
                        text = "Create account",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF2563EB)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Sign up to get started",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Google Button
                    Button(
                        onClick = {
                            googleLoading = true
                            launcher.launch(googleClient.signInIntent)
                        },
                        enabled = !googleLoading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B5563),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFF4B5563),
                            disabledContentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (googleLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.gicon),
                                    contentDescription = "Google Logo",
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp)
                                        .size(25.dp)
                                )
                                Text(
                                    text = "Sign up with Google",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // OR Divider
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            text = "  OR  ",
                            color = Color.Gray
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Name Field
                    OutlinedTextField(
                        value = state.signUpName,
                        onValueChange = {
                            viewModel.onEvent(AuthUiEvent.SignUpNameChanged(it))
                        },
                        placeholder = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.DarkGray,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedTextColor = Color.DarkGray,
                            unfocusedTextColor = Color.Gray,
                            cursorColor = Color.DarkGray
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email Field
                    OutlinedTextField(
                        value = state.signUpUsername,
                        onValueChange = {
                            viewModel.onEvent(AuthUiEvent.SignUpUsernameChanged(it))
                        },
                        placeholder = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.DarkGray,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedTextColor = Color.DarkGray,
                            unfocusedTextColor = Color.Gray,
                            cursorColor = Color.DarkGray
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    OutlinedTextField(
                        value = state.signUpPassword,
                        onValueChange = {
                            viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(it))
                        },
                        placeholder = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.DarkGray,
                            unfocusedIndicatorColor = Color.Gray,
                            focusedTextColor = Color.DarkGray,
                            unfocusedTextColor = Color.Gray,
                            cursorColor = Color.DarkGray
                        ),
                        singleLine = true,
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sign Up Button
                    Button(
                        onClick = { viewModel.onEvent(AuthUiEvent.SignUp) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB)
                        )
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Create account",
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sign Up
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            color = Color.Gray
                        )
                        TextButton(onClick = { navController.navigate("login") }) {
                            Text(
                                text = "Login",
                                color = Color(0xFF2563EB)
                            )
                        }
                    }
                }
            }
        }
    }
}

