package com.example.wishmart.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wishmart.viewmodel.ProductViewModel

@Composable
fun AirConditionerSection(
    navController: NavController,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val products = viewModel.airConditionerProducts


    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .background(Color.White)
            .padding(bottom = 20.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Air Conditioner",
                modifier = Modifier.padding(14.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Text(
                text = "See more",
                modifier = Modifier.padding(14.dp),
                color = Color(0xFF2563EB)
            )
        }


        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                AirConditionerProductCard(navController, product)
            }
        }
    }
}
