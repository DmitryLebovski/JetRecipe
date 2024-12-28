package com.example.jetrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetrecipe.domain.model.User
import com.example.jetrecipe.presentation.login.LoginScreen
import com.example.jetrecipe.presentation.main_screen.MainScreen
import com.example.jetrecipe.presentation.profile.ProfileScreen
import com.example.jetrecipe.ui.theme.JetRecipeTheme
import com.example.jetrecipe.utils.Routes.ACCOUNT_SCREEN
import com.example.jetrecipe.utils.Routes.LOGIN_SCREEN
import com.example.jetrecipe.utils.Routes.MAIN_SCREEN
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val currentFirebaseUser = Firebase.auth.currentUser
            val startDestination = if (currentFirebaseUser == null) {
                LOGIN_SCREEN
            } else {
                MAIN_SCREEN
            }

            val userState = remember { mutableStateOf<User?>(null) }

            if (currentFirebaseUser != null) {
                userState.value = User(
                    username = currentFirebaseUser.displayName ?: "Без имени",
                    email = currentFirebaseUser.email ?: "Нет e-mail",
                    profilePicture = currentFirebaseUser.photoUrl?.toString()
                )
            }

            JetRecipeTheme {
                Surface(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(LOGIN_SCREEN) {
                            LoginScreen(
                                onSignInSuccess = { newUser ->
                                    userState.value = newUser
                                    navController.navigate(MAIN_SCREEN) {
                                        popUpTo(LOGIN_SCREEN) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(MAIN_SCREEN) {
                            MainScreen(
                                navController = navController,
                                user = userState.value
                            )
                        }
                        composable(ACCOUNT_SCREEN) {
                            ProfileScreen(
                                user = userState.value,
                                onSignOut = {
                                    Firebase.auth.signOut()
                                    userState.value = null
                                    navController.navigate(LOGIN_SCREEN) {
                                        popUpTo(MAIN_SCREEN) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}