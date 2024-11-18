package com.taild.jetcartoon.ui.screens.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taild.domain.Character
import com.taild.domain.CharacterStatus
import com.taild.jetcartoon.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
):ViewModel() {
    val searchTextFieldState = TextFieldState()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchTextState: StateFlow<SearchState> = snapshotFlow { searchTextFieldState.text }
        .debounce(500)
        .mapLatest { if (it.isBlank()) SearchState.Empty else SearchState.UserQuery(it.toString()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SearchState.Empty
        )

    private val _state: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Empty)
    val state: StateFlow<ScreenState> = _state.asStateFlow()

    fun observeUserSearch() = viewModelScope.launch {
        searchTextState.collectLatest { searchState ->
            when (searchState) {
                is SearchState.Empty -> {
                    _state.value = ScreenState.Empty
                }

                is SearchState.UserQuery -> {
                    searchCharacters(name = searchState.query)
                }
            }
        }
    }

    fun toggleStatus(status: CharacterStatus) {
        _state.update {
            val currentState = it as? ScreenState.Content ?: return@update it
            val selectedStatus = currentState.filterState.selectedStatuses
            val newSelectedStatus = if (selectedStatus.contains(status)) {
                selectedStatus - status
            } else {
                selectedStatus + selectedStatus
            }

            currentState.copy(
                filterState = currentState.filterState.copy(
                    selectedStatuses = newSelectedStatus
                )
            )
        }
    }

    private fun searchCharacters(name: String) = viewModelScope.launch {
        _state.value = ScreenState.Searching
        characterRepository.fetchCharactersByName(
            name = name
        ).onSuccess { characters ->
            val allStatuses = characters.map {
                it.status
            }.toSet().sortedBy { it.displayName }
            _state.value = ScreenState.Content(
                userQuery = name,
                results = characters,
                filterState = ScreenState.Content.FilterState(
                    statuses = allStatuses,
                    selectedStatuses = emptyList()
                )
            )
        }.onFailure { e ->
            _state.value = ScreenState.Error(message = "No search results found")
        }
    }

    sealed interface SearchState {
        object Empty: SearchState
        data class UserQuery(val query: String): SearchState
    }

    sealed interface ScreenState {
        object Empty: ScreenState
        object Searching: ScreenState
        data class Error(val message: String): ScreenState
        data class Content(
            val userQuery: String,
            val results: List<Character>,
            val filterState: FilterState
        ): ScreenState {
            data class FilterState(
                val statuses: List<CharacterStatus>,
                val selectedStatuses: List<CharacterStatus>
            )
        }
    }
}
