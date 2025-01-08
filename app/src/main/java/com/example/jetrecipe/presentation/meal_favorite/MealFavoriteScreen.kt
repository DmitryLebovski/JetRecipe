package com.example.jetrecipe.presentation.meal_favorite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetrecipe.R
import com.example.jetrecipe.domain.model.Meal
import com.example.jetrecipe.domain.model.MealDetail
import com.example.jetrecipe.presentation.main_screen.ErrorScreen
import com.example.jetrecipe.presentation.main_screen.LoadingScreen
import com.example.jetrecipe.presentation.meal_detail.MealDetailScreen
import com.example.jetrecipe.reusable_components.MealExtendedCard
import com.example.jetrecipe.utils.RecipeUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealFavoriteScreen(
    viewModel: MealFavoriteViewModel = hiltViewModel()
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    LaunchedEffect(key1 = userId) {
        viewModel.loadFavoriteMeals(userId)
    }

    val favoriteMeals by viewModel.favoriteMeals.collectAsState()
    val uiState = viewModel.recipeUiState
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val collapsedFraction = scrollBehavior.state.collapsedFraction

    val nestedScrollConnection = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)

    when (uiState) {
        is RecipeUiState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }
        is RecipeUiState.Error -> {
            ErrorScreen(modifier = Modifier.fillMaxSize())
        }
        is RecipeUiState.Success -> {
            MealListContent(
                list = favoriteMeals,
                scrollBehavior = scrollBehavior,
                collapsedFraction = collapsedFraction,
                modifier = Modifier.then(nestedScrollConnection)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealListContent(
    list: List<MealDetail>,
    scrollBehavior: TopAppBarScrollBehavior,
    collapsedFraction: Float,
    modifier: Modifier
) {
    val textSize by animateFloatAsState(
        targetValue = if (collapsedFraction > 0.5f) 24f else 32f
    )

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf<MealDetail?>(null) }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState
        ) {
            MealDetailScreen(mealDetail = selectedMeal)
        }
    }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favorite_meals),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = textSize.sp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(list) { meal ->
                    MealExtendedCard(
                        Meal(
                            meal = meal.meal,
                            mealPictureURL = meal.mealPictureURL,
                            mealID = meal.mealID
                        ),
                        onClick = {
                            scope.launch {
                                selectedMeal = meal
                                isSheetOpen = true
                            }
                        }
                    )
                }
            }
        }
    )
}