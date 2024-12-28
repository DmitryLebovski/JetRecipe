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
fun ShimmerSource(){
    Box(modifier = Modifier
        .height(50.dp)
        .width(50.dp)
        .padding(8.dp)
        .clip(RoundedCornerShape(10.dp))
        .shimmerEffect()
    )
}