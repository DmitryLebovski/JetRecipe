package com.example.jetrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetrecipe.presentation.login.LoginScreen
import com.example.jetrecipe.presentation.main_screen.MainScreen
import com.example.jetrecipe.presentation.profile.ProfileScreen
import com.example.jetrecipe.ui.theme.JetRecipeTheme
import com.example.jetrecipe.utils.Routes.ACCOUNT_SCREEN
import com.example.jetrecipe.utils.Routes.LOGIN_SCREEN
import com.example.jetrecipe.utils.Routes.MAIN_SCREEN

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetRecipeTheme {
                Surface(modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = LOGIN_SCREEN
                    ) {
                        composable(
                            LOGIN_SCREEN
                        ) {
                            LoginScreen(
                                onSignInSuccess = {
                                    navController.navigate(MAIN_SCREEN)
                                }
                            )
                        }
                        composable(MAIN_SCREEN) {
                            MainScreen(
                                navController = navController
                            )
                        }
                        composable(ACCOUNT_SCREEN) {
                            ProfileScreen(
                                onSignOut = {
                                    navController.navigate(LOGIN_SCREEN)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
