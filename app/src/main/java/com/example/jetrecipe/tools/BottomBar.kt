package com.example.jetrecipe.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.jetrecipe.R
import com.example.jetrecipe.utils.Routes.FAVORITE_SCREEN
import com.example.jetrecipe.utils.Routes.MAIN_SCREEN

@Composable
fun BottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = navController.currentDestination?.route == MAIN_SCREEN,
            onClick = { navController.navigate(MAIN_SCREEN) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Settings") },
            selected = false,
            enabled = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorite") },
            selected = navController.currentDestination?.route == FAVORITE_SCREEN,
            enabled = true,
            onClick = { navController.navigate(FAVORITE_SCREEN) }
        )
    }
}