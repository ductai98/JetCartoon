@file:OptIn(ExperimentalFoundationApi::class)

package com.taild.jetcartoon.ui.screens.characterepisode

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taild.domain.Character
import com.taild.domain.Episode
import com.taild.jetcartoon.components.CharacterImage
import com.taild.jetcartoon.components.CharacterNameComponent
import com.taild.jetcartoon.components.DataPointComponent
import com.taild.jetcartoon.components.EpisodeRowComponent
import com.taild.jetcartoon.components.SimpleToolbar
import com.taild.jetcartoon.ui.screens.charaterdetail.DataPoint
import com.taild.jetcartoon.ui.screens.charaterdetail.LoadingState
import com.taild.jetcartoon.ui.theme.RickPrimary
import com.taild.jetcartoon.ui.theme.RickTextPrimary
import com.taild.network.KtorClient
import kotlinx.coroutines.launch

@Composable
fun CharacterEpisodeScreen(
    characterId: Int,
    ktorClient: KtorClient,
    onBackClick: () -> Unit
) {
    var characterState by remember {
        mutableStateOf<Character?>(null)
    }

    var episodesState by remember { mutableStateOf<List<Episode>>(emptyList()) }

    LaunchedEffect(Unit) {
        ktorClient
            .getCharacter(characterId)
            .onSuccess {
                characterState = it
                launch {
                    ktorClient.getEpisodes(it.episodeIds)
                        .onSuccess { episodes ->
                            episodesState = episodes
                        }
                        .onFailure {
                            //TODO handle exception
                        }
                }
            }.onFailure {
            //TODO handle exception
            }
    }

    characterState?.let { character ->
        Scaffold(
            topBar = {
                SimpleToolbar(
                    title = "Character Episodes",
                    onBackAction = onBackClick
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = RickPrimary) {
                EpisodesScreen(
                    character = character,
                    episodes = episodesState
                )
            }
        }

    } ?: LoadingState()
}

@Composable
private fun EpisodesScreen(
    character: Character,
    episodes: List<Episode>
) {
    val episodeBySeasonMap = episodes.groupBy { it.seasonNumber }
    LazyColumn(
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        item {
            CharacterNameComponent(name = character.name)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            LazyRow {
                episodeBySeasonMap.forEach { mapEntry -> 
                    val title = "Season ${mapEntry.key}"
                    val description = "${mapEntry.value.size} eps"
                    item {
                        DataPointComponent(dataPoint = DataPoint(title, description))
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            CharacterImage(url = character.imageUrl)
        }

        episodeBySeasonMap.forEach { mapEntry ->
            item { Spacer(modifier = Modifier.height(16.dp)) }
            stickyHeader { SeasonHeader(seasonNumber = mapEntry.key) }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(items = mapEntry.value) {episode ->
                EpisodeRowComponent(episode = episode)
            }
        }
    }
}

@Composable
private fun SeasonHeader(seasonNumber: Int) {
    Text(
        text = "Season $seasonNumber",
        color = RickTextPrimary,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = RickPrimary)
            .border(
                width = 1.dp,
                color = RickTextPrimary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp)
    )
}