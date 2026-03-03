package com.example.wishmart.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.wishmart.viewmodel.DealViewModel

@Composable
fun TodayBestDealSection(navController: NavController, viewModel: DealViewModel = hiltViewModel()) {

    val products by viewModel.products.collectAsState()

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
                text = "Today’s best deal",
                modifier = Modifier.padding(14.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Text(
                text = "See more",
                modifier = Modifier
                    .padding(14.dp)
                    .clickable {
                        navController.navigate("sale")
                    },
                color = Color(0xFF2563EB)
            )
        }

//        Spacer(modifier = Modifier.height(16.dp))

        LazyRow() {
            items(products) { product ->
                DealProductCardFromApi(navController, product)
            }
        }
    }
}
