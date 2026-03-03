package com.example.wishmart.ui.menu

// AllProductsScreen.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wishmart.product.ProductDto
import com.example.wishmart.ui.WishMartBottomBar
import com.example.wishmart.viewmodel.ProductsViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductsScreen(
    navController: NavController,
    viewModel: ProductsViewModel,
    onProductClick: (String) -> Unit
) {
    val state = viewModel.state

    var minText by remember(state.minPrice) { mutableStateOf(state.minPrice?.toString() ?: "") }
    var maxText by remember(state.maxPrice) { mutableStateOf(state.maxPrice?.toString() ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.category ?: "All Products",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
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
                },
                actions = {
                    TextButton(onClick = { viewModel.clearFilters() }) {
                        Text("Clear")
                    }
                }
            )
        },
        bottomBar = {
            WishMartBottomBar(navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF2F2F2)),
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
            ) {
                // ------- FILTER BAR -------
                FilterBar(
                    total = state.total,
                    selectedSort = state.sort ?: "latest",
                    onSortChange = { viewModel.setSort(it) },
                    minText = minText,
                    maxText = maxText,
                    onMinChange = { minText = it },
                    onMaxChange = { maxText = it },
                    onApplyPrice = {
                        val min = minText.trim().toIntOrNull()
                        val max = maxText.trim().toIntOrNull()
                        viewModel.setPrice(min, max)
                    }
                )

                Spacer(Modifier.height(10.dp))

                if (state.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                    return@Column
                }

                state.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(8.dp))
                }

                // ------- PRODUCTS GRID -------
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(state.items, key = { _, p -> p._id }) { index, p ->
                        ProductCard(
                            p = p,
                            onClick = { onProductClick(p._id) }
                        )

                        // ✅ Load more when reaching near the end
                        if (index == state.items.lastIndex - 3) {
                            LaunchedEffect(state.page, state.pages, state.items.size) {
                                viewModel.loadMore()
                            }
                        }
                    }

                    item {
                        if (state.isLoadingMore) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterBar(
    total: Int,
    selectedSort: String,
    onSortChange: (String) -> Unit,
    minText: String,
    maxText: String,
    onMinChange: (String) -> Unit,
    onMaxChange: (String) -> Unit,
    onApplyPrice: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text ="Total: $total",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.DarkGray
                )
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text ="Sort:",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    SortDropdown(selected = selectedSort, onSelected = onSortChange)
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = minText,
                    onValueChange = onMinChange,
                    label = { Text("Min") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFF2563EB),
                        unfocusedIndicatorColor = Color.DarkGray,
                        focusedLabelColor = Color(0xFF2563EB),
                        unfocusedLabelColor = Color.DarkGray,
                        cursorColor = Color(0xFF2563EB)
                    )
                )
                OutlinedTextField(
                    value = maxText,
                    onValueChange = onMaxChange,
                    label = { Text("Max") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFF2563EB),
                        unfocusedIndicatorColor = Color.DarkGray,
                        focusedLabelColor = Color(0xFF2563EB),
                        unfocusedLabelColor = Color.DarkGray,
                        cursorColor = Color(0xFF2563EB)
                    )
                )
                Button(
                    onClick = onApplyPrice,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2563EB),
                        contentColor = Color.White
                    )
                ) { Text("Apply") }
            }
        }
    }
}

@Composable
private fun SortDropdown(
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val options = listOf(
        "latest" to "Latest",
        "price-asc" to "Price ↑",
        "price-desc" to "Price ↓",
        "rating" to "Rating"
    )

    val label = options.firstOrNull { it.first == selected }?.second ?: "Latest"

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF2563EB),
                containerColor = Color.Transparent,
            ),

        ) { Text(label) }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (value, text) ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        expanded = false
                        onSelected(value)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
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