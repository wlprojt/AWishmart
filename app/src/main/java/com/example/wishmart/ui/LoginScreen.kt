package com.example.wishmart.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wishmart.R
import com.example.wishmart.auth.AuthResult
import com.example.wishmart.viewmodel.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }
    val state = viewModel.state
    val coroutineScope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    // ---------------- Google Sign-In Setup ----------------
    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("144637133530-s3k05e25lkl88md8k3hnemfamm5fe0to.apps.googleusercontent.com")
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, googleSignInOptions) }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                loading = true
                coroutineScope.launch {
                    try {
                        viewModel.onEvent(AuthUiEvent.GoogleSignIn(idToken))
                    } catch (e: Exception) {
                        Toast.makeText(context, "Backend login failed", Toast.LENGTH_SHORT).show()
                    } finally {
                        loading = false
                    }
                }
            } else {
                Toast.makeText(context, "Failed to get ID token", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
        }
    }

    // ---------------- Listen to Auth Results ----------------
    LaunchedEffect(Unit) {
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Success -> {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                }
                AuthResult.Unauthorized -> Toast.makeText(context, "Invalid email or password", Toast.LENGTH_LONG).show()
                AuthResult.Forbidden -> Toast.makeText(context, "Please verify your email", Toast.LENGTH_LONG).show()
                AuthResult.UnknownError -> Toast.makeText(context, "Backend login failed", Toast.LENGTH_LONG).show()
                else -> {}
            }
        }
    }

    // ---------------- UI ----------------
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
                .verticalScroll(rememberScrollState()),
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
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome back",
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                        color = Color(0xFF2563EB)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Login to continue", color = Color.Gray)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Google Sign-In Button
                    Button(
                        onClick = { googleLauncher.launch(googleSignInClient.signInIntent) },
                        enabled = !loading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B5563),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (loading) {
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
                                    text = "Sign in with Google",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = DividerDefaults.Thickness,
                            color = DividerDefaults.color
                        )
                        Text("  OR  ", color = Color.Gray)
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = DividerDefaults.Thickness,
                            color = DividerDefaults.color
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // Email Field
                    OutlinedTextField(
                        value = state.signInUsername,
                        onValueChange = { viewModel.onEvent(AuthUiEvent.SignInUsernameChanged(it)) },
                        placeholder = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
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
                        value = state.signInPassword,
                        onValueChange = { viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it)) },
                        placeholder = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
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
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        }
                    )

                    TextButton(
                        onClick = { navController.navigate("forgot") },
                        modifier = Modifier
                            .align(Alignment.End)
                            .offset(x = 0.dp, y = (-8).dp)
                    ) {
                        Text("Forgot Password?", color = Color(0xFF2563EB))
                    }

//                    Spacer(modifier = Modifier.height(12.dp))

                    // Login Button
                    Button(
                        onClick = { viewModel.onEvent(AuthUiEvent.SignIn) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        else Text("Login", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text("Don’t have an account? ", color = Color.Gray)
                        TextButton(onClick = { navController.navigate("signup") }) {
                            Text("Sign up", color = Color(0xFF2563EB))
                        }
                    }
                }
            }
        }
    }
}
