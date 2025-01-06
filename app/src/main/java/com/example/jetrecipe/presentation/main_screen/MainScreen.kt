package com.example.jetrecipe.presentation.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jetrecipe.R
import com.example.jetrecipe.domain.model.User
import com.example.jetrecipe.presentation.main_screen.components.GreetingPanel
import com.example.jetrecipe.presentation.profile.ProfileScreen
import com.example.jetrecipe.reusable_components.MealCard
import com.example.jetrecipe.reusable_components.ShimmerGreetingsCard
import com.example.jetrecipe.reusable_components.ShimmerMealCard
import com.example.jetrecipe.reusable_components.ShimmerText
import com.example.jetrecipe.utils.RecipeUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    user: User?,
    onSignOut: () -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
    recipeUiState: RecipeUiState = viewModel.recipeUiState
    ) {

    when (recipeUiState) {
        is RecipeUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is RecipeUiState.Success -> ResultScreen(navController, user, onSignOut, viewModel)
        is RecipeUiState.Error -> ErrorScreen( modifier = Modifier.fillMaxSize())
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ResultScreen(
    navController: NavController,
    user: User?,
    onSignOut: () -> Unit,
    viewModel: MainViewModel
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Рецепты '${category.category}'",
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )

                TextButton(
                    onClick = {}
                ) {
                    Text(
                        text = "Посмотреть все",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp),
                        maxLines = 1
                    )
                }
            }
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

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShimmerGreetingsCard()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.width(160.dp)
            ) {
                ShimmerText()
            }

            Box(
                modifier = Modifier.width(100.dp)
            ) {
                ShimmerText()
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShimmerMealCard()
            ShimmerMealCard()
            ShimmerMealCard()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShimmerMealCard()
            ShimmerMealCard()
            ShimmerMealCard()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShimmerMealCard()
            ShimmerMealCard()
            ShimmerMealCard()
        }
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(
            text = stringResource(id = R.string.loading_failed),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun PreviewLoadingScreen(){
    LoadingScreen()
}