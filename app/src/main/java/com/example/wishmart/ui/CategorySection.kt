package com.example.wishmart.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wishmart.R

@Composable
fun CategorySection(navController: NavController) {

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CategoryCard(
            navController,
            title = "Wireless\nheadphones",
            priceText = "Starting at $49",
            backgroundColor = Color(0xFFECEFF1),
            imageRes = R.drawable.headphone
        )

        CategoryCard(
            navController,
            title = "Grooming",
            priceText = "Starting at $49",
            backgroundColor = Color(0xFFECEFF1),
            imageRes = R.drawable.treamer
        )

        CategoryCard(
            navController,
            title = "Video games",
            priceText = "Starting at $49",
            backgroundColor = Color(0xFFF5E6C8),
            imageRes = R.drawable.games
        )
    }
}
