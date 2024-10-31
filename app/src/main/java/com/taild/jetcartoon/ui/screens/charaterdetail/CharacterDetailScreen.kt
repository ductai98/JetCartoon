package com.taild.jetcartoon.ui.screens.charaterdetail

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.taild.domain.Character
import com.taild.jetcartoon.components.CharacterDetailNamePlateComponent
import com.taild.jetcartoon.components.DataPointComponent
import com.taild.jetcartoon.ui.theme.RickAction
import com.taild.network.KtorClient
import kotlinx.coroutines.delay

@Composable
fun CharacterDetailScreen(
    ktorClient: KtorClient,
    characterId: Int,
    onAllEpisodesClick: (Int) -> Unit = {}
) {
    var character by remember { mutableStateOf<Character?>(null) }

    val characterDataPoints: List<DataPoint> by remember {
        derivedStateOf {
            buildList {
                character?.let {  character ->
                    add(DataPoint("Last known location", character.location.name))
                    add(DataPoint("Species", character.species))
                    add(DataPoint("Gender", character.gender.displayName))
                    character.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                    add(DataPoint("Origin", character.origin.name))
                    add(DataPoint("Episode count", character.episodeUrls.size.toString()))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(500)
        ktorClient
            .getCharacters(characterId)
            .onSuccess { data ->
                character = data
            }
            .onFailure {
                // TODO handle api exception
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        if (character == null) {
            item { LoadingState() }
            return@LazyColumn
        }

        item {
             CharacterDetailNamePlateComponent(
                 name = character!!.name,
                 status = character!!.status
             )
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        // coil image loader
        item {
            SubcomposeAsyncImage(
                model = character!!.imageUrl,
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

        items(characterDataPoints) {data ->
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
                        onAllEpisodesClick(characterId)
                    }
                    .padding(all = 8.dp)
                    .fillMaxWidth()
            )
        }

        item { Spacer(modifier = Modifier.height(64.dp)) }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 300.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(120.dp),
            color = RickAction
        )
    }

}

@Preview
@Composable
fun ScreenPreview() {
    CharacterDetailScreen(ktorClient = KtorClient(), characterId = 1)
}

data class DataPoint(
    val title: String,
    val description: String
)