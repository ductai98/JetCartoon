package com.taild.jetcartoon.ui.screens.allepisodes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taild.jetcartoon.NavDestination
import com.taild.jetcartoon.components.EpisodeRowComponent
import com.taild.jetcartoon.components.SimpleToolbar
import com.taild.jetcartoon.ui.screens.charaterdetail.LoadingState
import com.taild.jetcartoon.ui.theme.RickAction
import com.taild.jetcartoon.ui.theme.RickPrimary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllEpisodesScreen(
    viewModel: AllEpisodesViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refreshAllEpisodes()
    }

    when (val state = uiState) {
        AllEpisodesUiState.Error -> {
            // TODO
        }
        AllEpisodesUiState.Loading -> LoadingState()
        is AllEpisodesUiState.Success -> {
            Scaffold(
                topBar = {
                    SimpleToolbar(title = "All Episodes")
                }
            ){ innerPadding ->
                Surface(
                    modifier = Modifier.padding(innerPadding),
                    color = RickPrimary
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        state.data.forEach { mapEntry ->
                            val uniqueCharacterCount = mapEntry.value.flatMap {
                                it.characterIdsInEpisode
                            }.toSet().size
                            stickyHeader(key = mapEntry.key) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().background(color = RickPrimary)
                                ) {
                                    Text(
                                        text = mapEntry.key,
                                        color = Color.White,
                                        fontSize = 32.sp
                                    )
                                    Text(
                                        text = "$uniqueCharacterCount unique characters",
                                        color = Color.White,
                                        fontSize = 22.sp
                                    )
                                    Spacer(modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .padding(top = 4.dp)
                                        .background(
                                            color = RickAction,
                                            shape = RoundedCornerShape(2.dp)
                                        )
                                    )
                                }
                            }

                            items(
                                items = mapEntry.value,
                                key = { episode -> episode.id }
                            ) { episode ->
                                EpisodeRowComponent(episode = episode)
                            }
                        }
                    }
                }
            }
        }
    }
}