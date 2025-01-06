package com.example.jetrecipe.reusable_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerMealCard() {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(16.dp)
    ) {
        ShimmerImage(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(MaterialTheme.shapes.medium)
        )

        Spacer(Modifier.padding(top = 12.dp))

        ShimmerText()
    }
}