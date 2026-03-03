package com.example.wishmart.ui.menu


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wishmart.product.ProductDto
import com.example.wishmart.ui.WishMartBottomBar
import com.example.wishmart.viewmodel.SaleViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleScreen(
    navController: NavController,
    viewModel: SaleViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit
) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sale Products", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2563EB)),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.refresh() }) {
                        Text("Refresh", color = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            WishMartBottomBar(navController)
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF2F2F2))
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.error, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(10.dp))
                        Button(onClick = { viewModel.refresh() }) { Text("Try again") }
                    }
                }

                state.items.isEmpty() -> {
                    Text("No sale products found", modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp)
                            .padding(top = 12.dp)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.items, key = { it._id }) { p ->
                                SaleProductCard(
                                    p = p,
                                    onClick = { onProductClick(p._id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SaleProductCard(
    p: ProductDto,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(10.dp)) {

            AsyncImage(
                model = p.images.firstOrNull(),
                contentDescription = p.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                p.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(6.dp))

            val priceText = if (p.hasSale) {
                "$${p.displayPrice.roundToInt()}  (Sale)"
            } else {
                "$${p.displayPrice.roundToInt()}"
            }

            Text(
                text = priceText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(4.dp))
            Text(
                text = "⭐ ${p.rating} (${p.rating_count})",
                style = MaterialTheme.typography.labelSmall,
                color = Color.DarkGray
            )
        }
    }
}