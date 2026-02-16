package com.example.wishmart.ui.productDetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PriceSection(price: Double, salePrice: Double?) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        if (salePrice != null) {
            Text(
                text = "$$price",
                style = TextStyle(
                    textDecoration = TextDecoration.LineThrough,
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$$salePrice",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        } else {
            Text(
                text = "$$price",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
