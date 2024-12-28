package com.example.jetrecipe.tools

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowToastMessage(message: String?) {
    val context = LocalContext.current

    // Используем LaunchedEffect для показа Toast
    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}