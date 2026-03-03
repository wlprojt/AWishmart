package com.example.wishmart.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wishmart.viewmodel.CartUiState
import com.example.wishmart.viewmodel.CartViewModel
import com.google.firebase.auth.FirebaseAuth

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
    object Cart : BottomNavItem("cart", "Cart", Icons.Default.ShoppingCart)
    object Menu : BottomNavItem("menu", "Menu", Icons.Default.Menu)
}


@Composable
fun WishMartBottomBar(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel()
) {

    val state by cartViewModel.uiState.collectAsState()

    val cartCount = if (state is CartUiState.Success) {
        (state as CartUiState.Success).items.sumOf { it.qty }
    } else 0

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Cart,
        BottomNavItem.Menu
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {

        val currentRoute =
            navController.currentBackStackEntry?.destination?.route

        items.forEach { item ->

            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray,
                    selectedTextColor = Color(0xFF2563EB),
                    unselectedTextColor = Color(0xFF2563EB),
                    selectedIconColor = Color(0xFF2563EB),
                    unselectedIconColor = Color(0xFF2563EB),
                    ),
                selected = currentRoute?.startsWith(item.route) == true,
                onClick = {
                    when (item) {
                        is BottomNavItem.Profile -> {
                            val email = FirebaseAuth.getInstance()
                                .currentUser?.email ?: ""
                            navController.navigate(
                                "profile/${Uri.encode(email)}"
                            )
                        }
                        else -> {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                },
                icon = {
                    if (item is BottomNavItem.Cart && cartCount > 0) {

                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(cartCount.toString())
                                }
                            }
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        }

                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    }
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}
