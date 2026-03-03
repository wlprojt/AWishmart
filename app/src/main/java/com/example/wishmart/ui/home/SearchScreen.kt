package com.example.wishmart.ui.home


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wishmart.ui.WishMartBottomBar
import com.example.wishmart.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query = viewModel.query
    val suggestions = viewModel.suggestions

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF2563EB)),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
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
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF2F2F2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = viewModel::onQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search products") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.DarkGray,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFF2563EB),
                        unfocusedIndicatorColor = Color.DarkGray,
                        focusedLabelColor = Color(0xFF2563EB),
                        unfocusedLabelColor = Color.DarkGray,
                        cursorColor = Color(0xFF2563EB)
                    )
                )

                Spacer(Modifier.height(8.dp))

                LazyColumn {
                    items(suggestions, key = { it._id }) { item ->
                        SuggestionRow(
                            title = item.title,
                            image = item.images.firstOrNull(),
                            onClick = {
                                viewModel.clear()
                                navController.navigate("productDetails/${item._id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SuggestionRow(
    title: String,
    image: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AsyncImage(
            model = image,
            contentDescription = title,
            modifier = Modifier.size(48.dp)
        )

        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray
        )
    }
}