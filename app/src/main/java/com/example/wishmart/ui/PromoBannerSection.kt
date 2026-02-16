package com.example.wishmart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wishmart.R

@Composable
fun PromoBannerSection() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        PromoBannerCard(
            imageRes = R.drawable.sbgtwo,
            title = "The only case you need.",
            modifier = Modifier.weight(1f)
        )

        PromoBannerCard(
            imageRes = R.drawable.sbgone,
            title = "Get 30% OFF",
            modifier = Modifier.weight(1f)
        )
    }
}
