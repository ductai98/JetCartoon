package com.taild.domain

import androidx.compose.ui.graphics.Color


sealed class CharacterStatus(
    val displayName: String,
    val color: Color
) {
    object Alive: CharacterStatus("Alive", Color.Green)
    object Dead: CharacterStatus("Dead", Color.Red)
    object Unknown: CharacterStatus("Unknown", Color.Yellow)

    fun asColor(): Color {
        return when (this) {
            Alive -> Color.Green
            Dead -> Color.Red
            Unknown -> Color.Yellow
        }
    }
}