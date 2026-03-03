package com.example.wishmart.ui.cart

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wishmart.utils.findActivity
import com.example.wishmart.orders.BillingDto
import com.example.wishmart.orders.CreateOrderReq
import com.example.wishmart.orders.ItemReq
import com.example.wishmart.viewmodel.CartUiState
import com.example.wishmart.viewmodel.CartViewModel
import com.example.wishmart.viewmodel.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel()
) {
    val cartState by cartViewModel.uiState.collectAsState()
    val loading by checkoutViewModel.loading.collectAsState()
    val error by checkoutViewModel.error.collectAsState()

    // Billing state (simple)
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var apartment by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("India") }
    var state by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    // Payment result handler
    val context = LocalContext.current
    val activity = context.findActivity() ?: return

    val paymentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "Order placed ✅", Toast.LENGTH_SHORT).show()
            cartViewModel.clearCart()
            navController.navigate("order-success") {
                popUpTo("checkout") { inclusive = true } // use your route
            }
        } else {
            val err = result.data?.getStringExtra("errorDesc")
            Toast.makeText(context, err ?: "Payment cancelled/failed", Toast.LENGTH_SHORT).show()
        }
    }

    // show backend error
    LaunchedEffect(error) {
        if (!error.isNullOrBlank()) {
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2563EB)),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (cartState) {
            is CartUiState.Loading -> {
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is CartUiState.Error -> {
                val msg = (cartState as CartUiState.Error).message
                Box(
                    Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) { Text(msg) }
            }

            is CartUiState.Success -> {
                val items = (cartState as CartUiState.Success).items
                val total = cartViewModel.calculateTotal(items)

                if (items.isEmpty()) {
                    Box(
                        Modifier.fillMaxSize().padding(padding),
                        contentAlignment = Alignment.Center
                    ) { Text("Your cart is empty") }
                    return@Scaffold
                }

                val isFormValid = remember(
                    firstName, lastName, email, phone, address, country, state, city, postalCode, items
                ) {
                    firstName.isNotBlank() &&
                            lastName.isNotBlank() &&
                            email.isNotBlank() &&
                            phone.isNotBlank() &&
                            address.isNotBlank() &&
                            country.isNotBlank() &&
                            state.isNotBlank() &&
                            city.isNotBlank() &&
                            postalCode.isNotBlank() &&
                            items.isNotEmpty()
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card {
                            Column(
                                Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Billing Details", style = MaterialTheme.typography.titleMedium)

                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedTextField(
                                        value = firstName,
                                        onValueChange = { firstName = it },
                                        label = { Text("First name*") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = lastName,
                                        onValueChange = { lastName = it },
                                        label = { Text("Last name*") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }

                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    label = { Text("Email*") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                                )

                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { phone = it },
                                    label = { Text("Phone*") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                                )

                                OutlinedTextField(
                                    value = address,
                                    onValueChange = { address = it },
                                    label = { Text("Address*") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = apartment,
                                    onValueChange = { apartment = it },
                                    label = { Text("Apartment (optional)") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedTextField(
                                        value = country,
                                        onValueChange = { country = it },
                                        label = { Text("Country*") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = state,
                                        onValueChange = { state = it },
                                        label = { Text("State*") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }

                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedTextField(
                                        value = city,
                                        onValueChange = { city = it },
                                        label = { Text("City*") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    OutlinedTextField(
                                        value = postalCode,
                                        onValueChange = { postalCode = it },
                                        label = { Text("Postal Code*") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Card {
                            Column(
                                Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Order Summary", style = MaterialTheme.typography.titleMedium)

                                Text("Items: ${items.size}")
                                Text("Total: $${total.toInt()}", style = MaterialTheme.typography.titleLarge)

                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = isFormValid && !loading,
                                    onClick = {
                                        val req = CreateOrderReq(
                                            email = email,
                                            billing = BillingDto(
                                                firstName = firstName,
                                                lastName = lastName,
                                                phone = phone,
                                                address = address,
                                                apartment = apartment.ifBlank { null },
                                                country = country,
                                                state = state,
                                                city = city,
                                                postalCode = postalCode
                                            ),
                                            items = items.map { cart ->
                                                ItemReq(
                                                    productId = cart.productId, // ✅ FIXED
                                                    qty = cart.qty
                                                )
                                            },
                                            currency = "USD",       // ✅ dollars
                                            totalAmount = total     // ✅ REQUIRED for your backend right now
                                        )

                                        checkoutViewModel.placeOrderAndPayWithLauncher(
                                            activity = activity,
                                            launcher = paymentLauncher,
                                            email = email,
                                            phone = phone,
                                            createOrderReq = req,
                                            totalAmount = total.toInt()
                                        )
                                    }
                                ) {
                                    Text(if (loading) "Processing..." else "Pay Now")
                                }

                                if (!isFormValid) {
                                    Text(
                                        "Fill all required billing details.",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}