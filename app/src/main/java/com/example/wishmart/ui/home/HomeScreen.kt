package com.example.wishmart.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wishmart.R
import com.example.wishmart.ui.BrandsDealCard
import com.example.wishmart.ui.CategorySection
import com.example.wishmart.ui.PromoBannerSection
import com.example.wishmart.ui.ShopByCategoryCard
import com.example.wishmart.ui.TodayBestDealSection
import com.example.wishmart.ui.TopBrandsSection
import com.example.wishmart.ui.WishMartBottomBar
import com.example.wishmart.ui.category.AirConditionerSection
import com.example.wishmart.ui.category.AudioVideoSection
import com.example.wishmart.ui.category.GadgetsSection
import com.example.wishmart.ui.category.HomeAppliancesSection
import com.example.wishmart.ui.category.KitchenAppliancesSection
import com.example.wishmart.ui.category.PCsLaptopSection
import com.example.wishmart.ui.category.RefrigeratorSection
import com.example.wishmart.viewmodel.MainViewModel
import com.example.wishmart.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
    viewProModel: ProductViewModel = hiltViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.sllogo),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(26.dp)
                        )

                        Text(
                            text = "wishmart",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2563EB)),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("search")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            WishMartBottomBar(navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF2F2F2)),
        ) {
            LazyColumn(

            ) {
                item {
                    Image(
                        painter = painterResource(id = R.drawable.hcimg),
                        contentDescription = "hero image",
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(14.dp)
                        ) {

                            // 🔹 Logo Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.logoipsum), // replace with your logo
                                    contentDescription = "Logo",
                                    tint = Color.DarkGray,
//                                    modifier = Modifier.size(100.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 🔹 Big Title
                            Text(
                                text = "The best home entertainment system is here",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // 🔹 Subtitle
                            Text(
                                text = "Sit diam odio eget rhoncus volutpat est nibh velit posuere egestas.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 🔹 Shop Now Button (Text Style)
                            Text(
                                text = "Shop now →",
                                color = Color(0xFF2563EB),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        "allProducts?category=${android.net.Uri.encode("Audio & Video")}&page=1"
                                    )
                                }
                            )
                        }
                    }
                }
                item {
                    ShopByCategoryCard(navController)
                }
                item {
                    PromoBannerSection(navController)
                }
                item {
                    TodayBestDealSection(navController)
                }
                item {
                    CategorySection(navController)
                }
                item {
                    AudioVideoSection(navController)
                }
                item {
                    HomeAppliancesSection(navController)
                }
                item {
                    AirConditionerSection(navController)
                }
                item {
                    Image(
                        painter = painterResource(id = R.drawable.bannerone),
                        contentDescription = "hero image",
                    )
                }
                item {
                    KitchenAppliancesSection(navController)
                }
                item {
                    RefrigeratorSection(navController)
                }
                item {
                    Image(
                        painter = painterResource(id = R.drawable.bannertwo),
                        contentDescription = "hero image",
                    )
                }
                item {
                    PCsLaptopSection(navController)
                }
                item {
                    GadgetsSection(navController)
                }
                item {
                    BrandsDealCard(navController)
                }
                item {
                    TopBrandsSection()
                }
            }
        }
    }
}

