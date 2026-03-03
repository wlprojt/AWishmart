package com.example.wishmart

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wishmart.ui.ForgotPasswordScreen
import com.example.wishmart.ui.home.HomeScreen
import com.example.wishmart.ui.LoginScreen
import com.example.wishmart.ui.OtpScreen
import com.example.wishmart.ui.profile.ProfileScreen
import com.example.wishmart.ui.SignupScreen
import com.example.wishmart.ui.cart.CartScreen
import com.example.wishmart.ui.cart.CheckoutScreen
import com.example.wishmart.ui.cart.OrderSuccessScreen
import com.example.wishmart.ui.home.SearchScreen
import com.example.wishmart.ui.menu.AllProductsScreen
import com.example.wishmart.ui.menu.MenuScreen
import com.example.wishmart.ui.menu.SaleScreen
import com.example.wishmart.ui.productDetails.ProductDetailsScreen
import com.example.wishmart.viewmodel.MainViewModel
import com.example.wishmart.viewmodel.ProductViewModel
import com.example.wishmart.viewmodel.ProductsViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // 🔥 Reactive navigation
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {

            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = "profile/{email}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            // Decode the email from navigation arguments
            val email = Uri.decode(
                backStackEntry.arguments?.getString("email") ?: ""
            )

            ProfileScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable("signup") {
            SignupScreen(navController = navController, viewModel = viewModel)
        }

        composable("otp") {
            OtpScreen(navController = navController, viewModel = viewModel)
        }

        composable("forgot") {
            ForgotPasswordScreen(navController, viewModel)
        }

        composable("productDetails/{id}") { backStackEntry ->

            val id = backStackEntry.arguments?.getString("id")
            val viewModel: ProductViewModel = hiltViewModel()

            LaunchedEffect(id) {
                id?.let { viewModel.fetchProductById(it) }
            }

            val product = viewModel.selectedProduct

            product?.let {
                ProductDetailsScreen(
                    navController,
                    product = it,
                )
            }
        }

        composable("cart") {
            CartScreen(navController)
        }

        composable("menu") {
            MenuScreen(navController)
        }

        composable(
            route = "allProducts?category={category}&page={page}",
            arguments = listOf(
                navArgument("category") { nullable = true; defaultValue = null },
                navArgument("page") { defaultValue = "1" }
            )
        ) { backStackEntry ->

            val productsViewModel: ProductsViewModel = hiltViewModel()

            val category = backStackEntry.arguments?.getString("category")
            val page = backStackEntry.arguments?.getString("page")?.toIntOrNull() ?: 1

            // ✅ apply category + refresh once when category changes
            LaunchedEffect(category) {
                productsViewModel.setCategory(category)
                productsViewModel.refresh()
            }

            AllProductsScreen(
                navController = navController,
                viewModel = productsViewModel,
                onProductClick = { id ->
                    navController.navigate("productDetails/$id")
                }
            )
        }

        composable("sale") {
            SaleScreen(
                navController = navController,
                onProductClick = { id -> navController.navigate("productDetails/$id") }
            )
        }

        composable("search") {
            SearchScreen(navController)
        }

        composable("checkout") {
            CheckoutScreen(navController = navController, )
        }



        composable("orderSuccess") {
            OrderSuccessScreen(
                onGoHome = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

    }
}
