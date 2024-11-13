package com.taild.jetcartoon.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taild.jetcartoon.components.CharacterGridItem
import com.taild.jetcartoon.components.SimpleToolbar
import com.taild.jetcartoon.ui.screens.charaterdetail.LoadingState
import com.taild.jetcartoon.ui.theme.RickPrimary

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCharacterClick: (Int) -> Unit
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = viewModel) {
        viewModel.fetInitialPage()
    }

    val scrollState = rememberLazyGridState()

    val fetchNextPage: Boolean by remember {
        derivedStateOf {
            val currentCharacterCount =
                (viewState as? HomeScreenViewState.GridDisplay)?.characters?.size ?: return@derivedStateOf false
            val lastDisplayIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false

            return@derivedStateOf lastDisplayIndex >= currentCharacterCount - 10
        }
    }

    LaunchedEffect(key1 = fetchNextPage) {
        if (fetchNextPage) {
            viewModel.fetchNextPage()
        }
    }

    when (val state = viewState) {
        is HomeScreenViewState.Loading -> LoadingState()
        is HomeScreenViewState.GridDisplay -> {
            Scaffold(
                topBar = { SimpleToolbar(title = "All characters") }
            ) { innerPadding ->
                Surface(
                    modifier = Modifier.padding(innerPadding),
                    color = RickPrimary
                ) {
                    LazyVerticalGrid(
                        state = scrollState,
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.characters,
                            key = {item -> item.id}
                        ) {character ->
                            CharacterGridItem(
                                character = character,
                                onClick = { onCharacterClick(character.id) }
                            )
                        }
                    }
                }

            }
            
        }
    }
}