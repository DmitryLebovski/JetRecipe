package com.example.jetrecipe.presentation.meal_list

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetrecipe.R
import com.example.jetrecipe.domain.model.Meal
import com.example.jetrecipe.presentation.main_screen.ErrorScreen
import com.example.jetrecipe.presentation.main_screen.LoadingScreen
import com.example.jetrecipe.presentation.meal_detail.MealDetailScreen
import com.example.jetrecipe.reusable_components.MealExtendedCard
import com.example.jetrecipe.utils.RecipeUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealListScreen(
    navController: NavController,
    category: String,
    viewModel: MealListViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = category) {
        viewModel.loadMeals(category)
    }

    val mealList by viewModel.mealsByCategory.collectAsState()
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
                category = category,
                list = mealList,
                scrollBehavior = scrollBehavior,
                collapsedFraction = collapsedFraction,
                navController = navController,
                modifier = Modifier.then(nestedScrollConnection)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealListContent(
    category: String,
    list: List<Meal>,
    scrollBehavior: TopAppBarScrollBehavior,
    collapsedFraction: Float,
    navController: NavController,
    modifier: Modifier
) {
    val textSize by animateFloatAsState(
        targetValue = if (collapsedFraction > 0.5f) 24f else 32f
    )

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf<Int?>(null) }

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState
        ) {
            selectedMeal?.let { id ->
                MealDetailScreen(mealID = id)
            }
        }
    }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    if (collapsedFraction > 0.5f) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable {
                                        navController.popBackStack()
                                    }
                            )
                            Text(
                                text = stringResource(R.string.recipe_category),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = textSize.sp
                                )
                            )
                            Text(
                                text = category,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = textSize.sp
                                ),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    } else {
                        Column {
                            Text(
                                text = "Рецепты категории",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = textSize.sp
                                )
                            )
                            Text(
                                text = category,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = textSize.sp
                                ),
                            )
                        }
                    }
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
                    .fillMaxWidth()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(list) { meal ->
                    MealExtendedCard(
                        meal,
                        onClick = {
                            scope.launch {
                                selectedMeal = meal.mealID
                                isSheetOpen = true
                            }
                        }
                    )
                }
            }
        }
    )
}