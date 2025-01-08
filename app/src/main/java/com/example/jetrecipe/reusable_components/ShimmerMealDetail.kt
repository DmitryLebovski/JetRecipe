package com.example.jetrecipe.reusable_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jetrecipe.R

@Composable
fun ShimmerMealDetail(
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            ShimmerImage(
                modifier = Modifier.matchParentSize()
            )

            Icon(
                imageVector = Icons.Filled.Favorite ,
                tint = Color.White,
                contentDescription = "Favorite",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 8.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            Box(
                modifier = Modifier.width(160.dp)
            ) {
                ShimmerText()
            }

            Spacer(modifier = Modifier.padding(4.dp))

            Box(
                modifier = Modifier.width(100.dp)
            ) {
                ShimmerText()
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.instruction),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        ShimmerImage(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth()
                .padding(12.dp)
        )

    }
}
