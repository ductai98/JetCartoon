package com.taild.jetcartoon.ui.screens.allepisodes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AllEpisodesScreen(
    viewModel: AllEpisodesViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refreshAllEpisodes()
    }

    when (val state = uiState) {
        AllEpisodesUiState.Error -> TODO()
        AllEpisodesUiState.Loading -> TODO()
        is AllEpisodesUiState.Success -> TODO()
    }
}