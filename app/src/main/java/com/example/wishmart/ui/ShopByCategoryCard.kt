package com.example.wishmart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wishmart.R

@Composable
fun ShopByCategoryCard(navController: NavController) {

    val categories = listOf(
        Category("Air Conditioner", 4, R.drawable.ac),
        Category("Audio & Video", 5, R.drawable.tv),
        Category("Gadgets", 6, R.drawable.phone),
        Category("Home Appliances", 5, R.drawable.washing),
        Category("Kitchen Appliances", 6, R.drawable.oven),
        Category("PCs & Laptop", 4, R.drawable.laptop),
        Category("Refrigerator", 4, R.drawable.fridge),
        Category("Smart Home", 5, R.drawable.smart)
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // light grey background
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Text(
                text = "Shop by Category",
                modifier = Modifier.padding(vertical = 10.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.heightIn(max = 400.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(category, navController)
                }
            }
        }
    }
}
