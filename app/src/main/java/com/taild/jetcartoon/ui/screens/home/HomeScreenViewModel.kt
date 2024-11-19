package com.taild.jetcartoon.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taild.domain.Character
import com.taild.domain.CharacterPage
import com.taild.jetcartoon.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeScreenViewModel"

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {
    private val _state: MutableStateFlow<HomeScreenViewState> = MutableStateFlow(HomeScreenViewState.Loading)
    val state: StateFlow<HomeScreenViewState> = _state.asStateFlow()

    private val fetchedCharacterPages = mutableListOf<CharacterPage>()

    fun fetInitialPage() {
        _state.value = HomeScreenViewState.Loading
        viewModelScope.launch {
            if (fetchedCharacterPages.isNotEmpty()) {
                val characters = fetchedCharacterPages.flatMap { it.results }
                _state.value = HomeScreenViewState.GridDisplay(characters = characters)
                return@launch
            }
            val initialPage = characterRepository.fetchCharacterPage(1)
            initialPage.onSuccess { page ->
                fetchedCharacterPages.clear()
                fetchedCharacterPages.add(page)
                _state.value = HomeScreenViewState.GridDisplay(characters = page.results)
            }.onFailure {
                //TODO handle exception
            }
        }
    }

    fun fetchNextPage() {
        viewModelScope.launch {
            val nextPageIndex = fetchedCharacterPages.size + 1
            Log.d(TAG, "fetchNextPage: page = $nextPageIndex")
            characterRepository.fetchCharacterPage(nextPageIndex).onSuccess { page ->
                fetchedCharacterPages.add(page)
                val currentCharacters = (_state.value as HomeScreenViewState.GridDisplay).characters
                val characters = currentCharacters + page.results
                _state.value = HomeScreenViewState.GridDisplay(characters = characters)
            }.onFailure {
                //TODO handle exception
            }
        }
    }
}

sealed interface HomeScreenViewState {
    object Loading: HomeScreenViewState
    data class GridDisplay(
        val characters: List<Character> = emptyList()
    ): HomeScreenViewState
}