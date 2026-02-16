package com.example.wishmart.ui

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wishmart.auth.AuthResult
import com.example.wishmart.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OtpScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
) {
    val context = LocalContext.current
    val state = viewModel.state

    var otp by remember { mutableStateOf("") }

    // 🔁 Listen to OTP verification result
    LaunchedEffect(Unit) {
        viewModel.authResults.collectLatest { result ->
            when (result) {
                AuthResult.OtpVerified -> {
                    Toast.makeText(
                        context,
                        "OTP Verified.",
                        Toast.LENGTH_LONG
                    ).show()

//                    val email = viewModel.email.value ?: state.signUpUsername

                    val email = viewModel.email.value

                    if (!email.isNullOrEmpty()) {

                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                            launchSingleTop = true
                        }
                    }

                }

                AuthResult.Unauthorized -> {
                    Toast.makeText(
                        context,
                        "Invalid OTP",
                        Toast.LENGTH_LONG
                    ).show()
                }

                AuthResult.UnknownError -> {
                    Toast.makeText(
                        context,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }

                else -> Unit
            }
        }
    }

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

                    Text(
                        text = "Verify OTP",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF2563EB)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Enter the 6-digit code sent to your email",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    OutlinedTextField(
                        value = otp,
                        onValueChange = {
                            if (it.length <= 6) otp = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("OTP Code") },
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

//                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = {
                            viewModel.onEvent(
                                AuthUiEvent.ResendOtp(
                                    email = state.signUpUsername
                                )
                            )
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Resend OTP", color = Color(0xFF2563EB))
                    }

//                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            viewModel.onEvent(
                                AuthUiEvent.VerifyOtp(
                                    email = state.signUpUsername,
                                    otp = otp
                                )
                            )
                        },
                        enabled = otp.length == 6 && !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB)
                        )
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text("Verify", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}