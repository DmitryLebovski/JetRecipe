package com.example.jetrecipe.reusable_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jetrecipe.R


@Composable
fun ShimmerGreetingsCard() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Home Icon",
                )

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Text(
                    text = "Добро пожаловать,",
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.inter))
                )

            }
            ShimmerImage(modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .width(260.dp)
                .height(25.dp)
        ) {
            ShimmerText()
        }
    }
}