package com.taild.jetcartoon.ui.screens.charaterdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taild.domain.Character
import com.taild.jetcartoon.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {
    private val _characterDetailViewState = MutableStateFlow<CharacterDetailViewState>(CharacterDetailViewState.Loading)
    val characterDetailViewState = _characterDetailViewState.asStateFlow()

    fun fetCharacter(characterId: Int) = viewModelScope.launch {
        _characterDetailViewState.value = CharacterDetailViewState.Loading
        characterRepository.fetchCharacter(characterId).onSuccess { character ->
            val dataPoint = buildList {
                character.let { character ->
                    add(DataPoint("Last known location", character.location.name))
                    add(DataPoint("Species", character.species))
                    add(DataPoint("Gender", character.gender.displayName))
                    character.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                    add(DataPoint("Origin", character.origin.name))
                    add(DataPoint("Episode count", character.episodeIds.size.toString()))
                }
            }
            _characterDetailViewState.value = CharacterDetailViewState.Success(character, dataPoint)
        }.onFailure { exception ->
            _characterDetailViewState.value = CharacterDetailViewState.Error(exception.message ?: "Unknown error")
        }
    }
}

sealed interface CharacterDetailViewState {
    object Loading: CharacterDetailViewState
    data class Error(val message: String): CharacterDetailViewState
    data class Success(
        val character: Character,
        val characterDataPoints: List<DataPoint>
    ): CharacterDetailViewState
}