package com.example.wishmart.ui.productDetails


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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wishmart.products.sale.ProductResponse
import com.example.wishmart.ui.StarRating

@Composable
fun RelatedProductCard(
    product: ProductResponse,
    navController: NavController
) {

    val hasDiscount = product.sale_price != null

    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable {
                navController.navigate("productDetails/${product._id}") {
                    launchSingleTop = true
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // 🖼 Image
            AsyncImage(
                model = product.images?.firstOrNull(),
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ⭐ Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                StarRating(
                    rating = product.rating,
                    starSize = 14.dp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "(${product.rating_count})",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 🏷 Title
            Text(
                text = product.title,
                maxLines = 2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 💰 Price
            if (hasDiscount) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "$${product.price}",
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "$${product.sale_price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                Text(
                    text = "$${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
