package com.example.wishmart.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class Category(
    val title: String,
    val productCount: Int,
    val imageRes: Int
)


@Composable
fun CategoryItem(category: Category, navController: NavController) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
//            .padding(14.dp)
            .clickable {
                navController.navigate(
                    "allProducts?category=${android.net.Uri.encode(category.title)}&page=1"
                )
            }
    ) {

        Image(
            painter = painterResource(id = category.imageRes),
            contentDescription = category.title,
            modifier = Modifier
                .size(150.dp)
        )

//        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = category.title.uppercase(),
            modifier = Modifier.offset(0.dp, (-37).dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

//        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${category.productCount} Products",
            modifier = Modifier.offset(0.dp, (-37).dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

//        Spacer(modifier = Modifier.height(4.dp))
    }
}
