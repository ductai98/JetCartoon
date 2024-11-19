package com.taild.jetcartoon.ui.screens.charaterdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.taild.domain.Character
import com.taild.jetcartoon.components.CharacterDetailNamePlateComponent
import com.taild.jetcartoon.components.DataPointComponent
import com.taild.jetcartoon.components.SimpleToolbar
import com.taild.jetcartoon.ui.theme.RickAction
import com.taild.jetcartoon.ui.theme.RickPrimary

@Composable
fun CharacterDetailScreen(
    viewModel: CharacterDetailViewModel = hiltViewModel(),
    characterId: Int,
    onAllEpisodesClick: (Int) -> Unit = {},
    onBackClick: () -> Unit
) {
    val state by viewModel.characterDetailViewState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetCharacter(characterId)
    }

    Scaffold(
        topBar = {
            SimpleToolbar(
                title = "Character Detail",
                onBackAction = onBackClick
            )
        }
    ) {
        Surface(
            modifier = Modifier.padding(it),
            color = RickPrimary
        ) {
            when (val viewState = state) {
                is CharacterDetailViewState.Error -> {
                    // TODO Handle Error
                }
                CharacterDetailViewState.Loading -> {
                    LoadingState()
                }
                is CharacterDetailViewState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(RickPrimary),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        val character = viewState.character
                        val dataPoint = viewState.characterDataPoints
                        item {
                            CharacterDetailNamePlateComponent(
                                name = character.name,
                                status = character.status
                            )
                        }

                        item { Spacer(modifier = Modifier.height(8.dp)) }

                        // coil image loader
                        item {
                            SubcomposeAsyncImage(
                                model = character.imageUrl,
                                contentDescription = "Character image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp)),
                                loading = {
                                    LoadingState()
                                }
                            )
                        }


                        items(items = dataPoint) { data ->
                            Spacer(modifier = Modifier.height(32.dp))
                            DataPointComponent(dataPoint = data)
                        }

                        item { Spacer(modifier = Modifier.height(32.dp)) }

                        // Button
                        item {
                            Text(
                                text = "View all episodes",
                                color = RickAction,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .border(
                                        width = 1.dp,
                                        color = RickAction,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .clickable {
                                        onAllEpisodesClick(character.id)
                                    }
                                    .padding(all = 8.dp)
                                    .fillMaxWidth()
                            )
                        }

                        item { Spacer(modifier = Modifier.height(64.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(120.dp),
            color = RickAction
        )
    }

}

data class DataPoint(
    val title: String,
    val description: String
)