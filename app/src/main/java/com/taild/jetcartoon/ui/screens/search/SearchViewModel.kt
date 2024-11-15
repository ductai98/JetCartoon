package com.taild.jetcartoon.ui.screens.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor():ViewModel() {
    val searchTextFieldState = TextFieldState()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchTextState = snapshotFlow { searchTextFieldState.text }
        .debounce(500)
        .mapLatest { if (it.isBlank()) "Await your command ..." else it.toString() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )
}