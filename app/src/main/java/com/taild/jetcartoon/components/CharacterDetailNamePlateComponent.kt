package com.taild.jetcartoon.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.taild.domain.CharacterStatus
import com.taild.jetcartoon.ui.theme.RickAction

@Composable
fun CharacterDetailNamePlateComponent(name: String, status: CharacterStatus) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CharacterStatusComponent(characterStatus = status)
        Text(
            text = name,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 42.sp,
            color = RickAction,
        )
    }
}

@Preview
@Composable
fun NamePlatePreview() {
    CharacterDetailNamePlateComponent(
        name = "Rick Sanchez Rick Sanchez Rick Sanchez",
        status = CharacterStatus.Alive
    )
}