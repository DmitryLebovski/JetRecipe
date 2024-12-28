package com.nikitacherenkov.newsapp.presentation.reusable_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nikitacherenkov.newsapp.presentation.tools.shimmerEffect

@Composable
fun ShimmerText(){
    Box(
        modifier = Modifier
            .height(18.dp)
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(12.dp))
            .shimmerEffect()
    )
}