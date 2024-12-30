package com.example.jetrecipe.presentation.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetrecipe.domain.model.User
import com.example.jetrecipe.presentation.main_screen.components.GreetingPanel
import com.example.jetrecipe.presentation.profile.ProfileScreen
import com.example.jetrecipe.reusable_components.MealCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    user: User?,
    onSignOut: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }

    val categories by viewModel.categories.collectAsState()
    val mealsByCategory by viewModel.mealsByCategory.collectAsState()


    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState
        ) {
            ProfileScreen(
                user = user,
                onSignOut = {
                    scope.launch {
                        sheetState.hide()
                        isSheetOpen = false
                    }
                    onSignOut()
                }
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            GreetingPanel(navController, user, onIconClick = {
                scope.launch {
                    isSheetOpen = true
                }
            })
        }

        items(categories) { category ->
            Text(
                text = category.category,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(8.dp)
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mealsByCategory[category.category] ?: emptyList()) { meal ->
                    MealCard(meal)
                }
            }
        }
    }
}