package com.nikitacherenkov.newsapp.presentation.reusable_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nikitacherenkov.newsapp.presentation.tools.shimmerEffect

@Composable
fun ShimmerImage(
    modifier: Modifier
){
    Box(modifier = modifier
        .height(200.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .shimmerEffect()
    )
}