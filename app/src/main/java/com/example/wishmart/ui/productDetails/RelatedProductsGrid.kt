package com.example.wishmart.ui.productDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wishmart.products.sale.ProductResponse

@Composable
fun RelatedProductsSection(
    products: List<ProductResponse>,
    currentProductId: String,
    navController: NavController
) {

    // 🚀 Remove current product
    val filteredProducts = products
        .filter { it._id != currentProductId }
        .distinctBy { it._id }
        .take(6)

    if (filteredProducts.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = "Related Products",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(horizontal = 14.dp),
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = filteredProducts,
                key = { it._id }   // 🔥 important
            ) { product ->

                RelatedProductCard(
                    product = product,
                    navController = navController
                )
            }
        }
    }
}

