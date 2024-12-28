package com.example.jetrecipe.presentation.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetrecipe.domain.model.User
import com.example.jetrecipe.utils.Routes.ACCOUNT_SCREEN

@Composable
fun MainScreen(
    navController: NavController,
    user: User?
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "Main Screen",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        user?.let {
            Text(text = "Добро пожаловать, ${it.username}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate(ACCOUNT_SCREEN) }) {
            Text(text = "Go to Account")
        }
    }
}