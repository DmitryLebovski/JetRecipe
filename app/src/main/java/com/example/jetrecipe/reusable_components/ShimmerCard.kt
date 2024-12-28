package com.nikitacherenkov.newsapp.presentation.reusable_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ShimmerCard(){
    Card(
        colors = CardDefaults.cardColors(Color(0xFFEEEEEE)),
        elevation = CardDefaults.cardElevation(15.dp),
        modifier = Modifier
            .padding(20.dp)
            .height(300.dp)
    ){
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1f) // Задаем weight для Row
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    ShimmerImage(modifier = Modifier.padding(10.dp))
                }
                ShimmerSource()
            }
            ShimmerText()
            Spacer(modifier = Modifier.height(10.dp))
            ShimmerText()
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}