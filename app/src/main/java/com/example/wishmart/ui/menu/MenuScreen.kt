package com.example.wishmart.ui.menu

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wishmart.ui.WishMartBottomBar
import java.net.URLEncoder

data class CategoryItem(val label: String)

private val categories = listOf(
    CategoryItem("All products"),
    CategoryItem("Air Conditioner"),
    CategoryItem("Audio & Video"),
    CategoryItem("Gadgets"),
    CategoryItem("Home Appliances"),
    CategoryItem("Kitchen Appliances"),
    CategoryItem("PCs & Laptop"),
    CategoryItem("Refrigerator"),
    CategoryItem("Smart Home"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    selectedCategory: String? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2563EB)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = { WishMartBottomBar(navController) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF2F2F2))
        ) {
            items(categories) { item ->

                val isSelected =
                    selectedCategory?.equals(item.label, ignoreCase = true) == true

                MenuItemRow(
                    title = item.label,
                    isSelected = isSelected,
                    onClick = {
                        val route =
                            if (item.label == "All products") "allProducts?page=1"
                            else "allProducts?category=${Uri.encode(item.label)}&page=1"

                        navController.navigate(route)
                    }
                )

                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}

@Composable
private fun MenuItemRow(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val weight =  FontWeight.Normal
    val textColor = Color.DarkGray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Text(
            text = title,
            fontWeight = weight,
            color = textColor
        )
    }
}

private fun encode(value: String): String =
    URLEncoder.encode(value, "UTF-8")