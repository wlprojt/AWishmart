package com.example.wishmart.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wishmart.ui.WishMartBottomBar
import com.example.wishmart.viewmodel.CartUiState
import com.example.wishmart.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart") },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2563EB)),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            WishMartBottomBar(navController)
        }
    ) { padding ->

        when (state) {

            is CartUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFF2F2F2)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CartUiState.Error -> {
                val message = (state as CartUiState.Error).message
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFF2F2F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = message)
                }
            }

            is CartUiState.Success -> {

                val items = (state as CartUiState.Success).items
                val total = viewModel.calculateTotal(items)

                if (items.isEmpty()) {
                    EmptyCart()
                } else {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .background(Color(0xFFF2F2F2))
                    ) {

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = items,
                                key = { it._id }
                            ) { item ->

                                CartItemCard(
                                    item = item,
                                    onIncrease = {
                                        viewModel.updateQty(
                                            item._id,
                                            item.qty + 1
                                        )
                                    },
                                    onDecrease = {
                                        viewModel.updateQty(
                                            item._id,
                                            item.qty - 1
                                        )
                                    },
                                    onRemove = {
                                        viewModel.removeItem(item._id)
                                    }
                                )
                            }
                        }

                        CartSummary(
                            total = total,
                            onCheckoutClick = {
                                navController.navigate("checkout")
                            }
                        )
                    }
                }
            }
        }
    }
}
