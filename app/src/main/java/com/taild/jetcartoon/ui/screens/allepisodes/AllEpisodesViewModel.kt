package com.taild.jetcartoon.ui.screens.allepisodes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taild.domain.Episode
import com.taild.jetcartoon.repository.EpisodesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllEpisodesViewModel @Inject constructor(
    private val episodesRepository: EpisodesRepository
): ViewModel() {
    private val _state: MutableStateFlow<AllEpisodesUiState> = MutableStateFlow(AllEpisodesUiState.Loading)
    val state = _state.asStateFlow()

    fun refreshAllEpisodes(
        forceRefresh: Boolean = false
    ) = viewModelScope.launch {
        if (forceRefresh) {
            _state.value = AllEpisodesUiState.Loading
        }

        episodesRepository.fetchAllEpisodes().onSuccess { episodes ->
            val data = episodes.groupBy { it.seasonNumber.toString() }.mapKeys {
                "Season ${it.key}"
            }
            _state.value = AllEpisodesUiState.Success(data = data)
        }.onFailure {
            _state.value = AllEpisodesUiState.Error
        }
    }
}

sealed interface AllEpisodesUiState {
    object Error: AllEpisodesUiState
    object Loading: AllEpisodesUiState
    data class Success(val data: Map<String, List<Episode>>): AllEpisodesUiState
}