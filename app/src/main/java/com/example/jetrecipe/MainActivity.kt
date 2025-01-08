package com.example.jetrecipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.jetrecipe.domain.model.User
import com.example.jetrecipe.presentation.login.LoginScreen
import com.example.jetrecipe.presentation.main_screen.MainScreen
import com.example.jetrecipe.presentation.meal_favorite.MealFavoriteScreen
import com.example.jetrecipe.presentation.meal_list.MealListScreen
import com.example.jetrecipe.tools.BottomBar
import com.example.jetrecipe.ui.theme.JetRecipeTheme
import com.example.jetrecipe.utils.Routes.FAVORITE_SCREEN
import com.example.jetrecipe.utils.Routes.LOGIN_SCREEN
import com.example.jetrecipe.utils.Routes.MAIN_SCREEN
import com.example.jetrecipe.utils.Routes.MEAL_LIST_SCREEN
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
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
                    val currentBackStackEntry = navController.currentBackStackEntryAsState()

                    Scaffold (
                        bottomBar = {
                            val currentRoute = currentBackStackEntry.value?.destination?.route
                            if (currentRoute == MAIN_SCREEN || currentRoute == FAVORITE_SCREEN) {
                                BottomBar(navController)
                            }
                        }
                    ){ paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200))},
                            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200))},
                            popEnterTransition =  { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(200))},
                            popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(200))},

                            modifier = Modifier.padding(paddingValues)
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
                            composable(FAVORITE_SCREEN) {
                                MealFavoriteScreen()
                            }
                            composable(
                                route = "${MEAL_LIST_SCREEN}/{category}",
                                arguments = listOf(navArgument("category") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val category = backStackEntry.arguments?.getString("category") ?: ""
                                MealListScreen(
                                    navController = navController,
                                    category = category
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
