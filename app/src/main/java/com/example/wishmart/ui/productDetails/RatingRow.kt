package com.example.wishmart.ui.productDetails


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingRow(rating: Float, count: Int) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating.toInt())
                    Icons.Default.Star
                else
                    Icons.Default.StarBorder,
                contentDescription = null,
                tint = Color(0xFFFFC107)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "($count)",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
