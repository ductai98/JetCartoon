package com.taild.jetcartoon.ui.screens.search

import android.view.RoundedCorner
import android.widget.Spinner
import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.delete
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taild.domain.CharacterStatus
import com.taild.jetcartoon.components.CharacterListItem
import com.taild.jetcartoon.components.SimpleToolbar
import com.taild.jetcartoon.ui.screens.charaterdetail.DataPoint
import com.taild.jetcartoon.ui.screens.charaterdetail.LoadingState
import com.taild.jetcartoon.ui.theme.RickAction
import com.taild.jetcartoon.ui.theme.RickPrimary
import kotlinx.serialization.json.JsonNull.content

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {

    DisposableEffect(Unit) {
        val job = viewModel.observeUserSearch()
        onDispose { job.cancel() }
    }

    Scaffold(
        topBar = {
            SimpleToolbar(title = "Search")
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = RickPrimary
                    )
                    BasicTextField(
                        state = viewModel.searchTextFieldState,
                        modifier = Modifier.weight(1f),
                    )

                    AnimatedVisibility(
                        visible = viewModel.searchTextFieldState.text.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            tint = Color.Gray,
                            modifier = Modifier.clickable {
                                viewModel.searchTextFieldState.edit {
                                    delete(0, length)
                                }
                            }
                        )
                    }
                }

                val uiState by viewModel.state.collectAsStateWithLifecycle()

                when (val state = uiState) {
                    SearchViewModel.ScreenState.Empty -> SearchMessage()
                    SearchViewModel.ScreenState.Searching -> LoadingState()
                    is SearchViewModel.ScreenState.Content -> SearchScreenContent(
                        content = state,
                        onStatusClick = viewModel::toggleStatus
                    )
                    is SearchViewModel.ScreenState.Error -> {
                        SearchMessage(text = state.message)
                        Button(
                            colors = ButtonDefaults.buttonColors().copy(contentColor = RickAction),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 84.dp),
                            onClick = { viewModel.searchTextFieldState.clearText() }
                        ) {
                            Text(text = "Clear search", color = RickPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreenContent(
    content: SearchViewModel.ScreenState.Content,
    onStatusClick: (CharacterStatus) -> Unit = {}
) {
    Text(
        text = "${content.results.size} results for '${content.userQuery}'",
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp),
        textAlign = TextAlign.Start,
        fontSize = 18.sp
    )
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content.filterState.statuses.forEach { status ->
            val selected = content.filterState.selectedStatuses.contains(status)
            val color = if (selected) RickAction else Color.LightGray
            val count = content.results.count { it.status == status }
            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = color,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clickable {
                        onStatusClick(status)
                    }.clip(RoundedCornerShape(8.dp)),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$count",
                    color = RickPrimary,
                    modifier = Modifier
                        .background(color = color)
                        .padding(8.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp
                )
                Text(
                    text = status.displayName,
                    color = Color.White,
                    modifier = Modifier
                        .padding(6.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp
                )
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        val filteredStatus = content.filterState.selectedStatuses
        val filteredList = content.results.filter { filteredStatus.contains(it.status) }
        items(
            items = filteredList,
            key = { it.id }
        ) { character ->
            val dataPoints = buildList {
                add(DataPoint("Last known location", character.location.name))
                add(DataPoint("Species", character.species))
                add(DataPoint("Gender", character.gender.displayName))
                character.type.takeIf { it.isNotEmpty() }?.let { type ->
                    add(DataPoint("Type", type))
                }
                add(DataPoint("Origin", character.origin.name))
                add(DataPoint("Episode count", character.episodeIds.size.toString()))
            }

            CharacterListItem(
                modifier = Modifier.animateItem(),
                character = character,
                characterDataPoints = dataPoints,
                onClick = {
                    // TODO
                },
            )
        }
    }
}

@Composable
fun SearchMessage(text: String = "Search for characters!") {
    Text(
        text = text,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        textAlign = TextAlign.Center,
        fontSize = 26.sp
    )
}