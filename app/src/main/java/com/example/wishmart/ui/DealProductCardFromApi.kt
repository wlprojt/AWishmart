package com.example.wishmart.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wishmart.products.sale.ProductResponse

@Composable
fun DealProductCardFromApi(navController: NavController, product: ProductResponse) {

    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(start = 16.dp)
            .clickable{
                navController.navigate("productDetails/${product._id}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {

        Column(Modifier.padding(12.dp)) {

            product.images?.firstOrNull()?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ⭐ Rating Row
            Row(verticalAlignment = Alignment.CenterVertically) {

                StarRating(
                    rating = product.rating,
                    starSize = 14.dp
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "(${product.rating_count})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }


            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = product.title,
                maxLines = 2,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                if (product.sale_price != null) {
                    Text(
                        text = "$${product.price}",
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "$${product.sale_price}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
