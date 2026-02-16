package com.example.wishmart.ui.productDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wishmart.products.sale.ProductResponse
import com.example.wishmart.ui.StarRating
import com.example.wishmart.ui.WishMartBottomBar
import com.example.wishmart.viewmodel.CartViewModel
import com.example.wishmart.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    navController: NavController,
    product: ProductResponse,
    viewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel()
) {

    LaunchedEffect(product._id) {
        viewModel.fetchRelatedProducts(
            category = product.category,
            currentId = product._id
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Product Details",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2563EB)),
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF2F2F2))
        ) {

            // 🖼 Image Section
            item {
                ProductImageSection(product.images)
            }

            // 📦 Product Info
            item {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {

                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (product.stock > 0)
                            "In Stock (${product.stock})"
                        else
                            "Out of Stock",
                        color = if (product.stock > 0)
                            Color(0xFF2E7D32)  // Green
                        else
                            Color.Red,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // ⭐ Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StarRating(
                            rating = product.rating,
                            starSize = 18.dp
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "(${product.rating_count})",
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 💰 Price
                    if (product.sale_price != null) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(
                                text = "$${product.price}",
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "$${product.sale_price}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        }

                    } else {
                        Text(
                            text = "$${product.price}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }



                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = {
                            cartViewModel.addToCart(product)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB)
                        )
                    ) {
                        Text(
                            text = "Add to Cart",
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    SafeCheckoutSection()

                    ProductInfoTabs(product.description)

                    Spacer(modifier = Modifier.height(16.dp))

                    RelatedProductsSection(
                        products = viewModel.relatedProducts,
                        currentProductId = product._id,
                        navController = navController
                    )

                }
            }
        }
    }
}
