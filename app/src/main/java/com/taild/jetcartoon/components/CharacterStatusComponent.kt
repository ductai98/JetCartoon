package com.taild.jetcartoon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taild.domain.CharacterStatus
import com.taild.jetcartoon.ui.theme.RickPrimary
import com.taild.jetcartoon.ui.theme.RickTextPrimary

@Composable
fun CharacterStatusComponent(
    characterStatus: CharacterStatus,
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = characterStatus.color,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                top = 12.dp,
                bottom = 12.dp,
                start = 12.dp,
                end = 48.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Status: ${characterStatus.displayName}",
            fontSize = 20.sp,
            color = RickTextPrimary
        )
    }
}

@Preview
@Composable
fun CharacterStatusAlivePreview() {
    CharacterStatusComponent(characterStatus = CharacterStatus.Alive)
}

@Preview
@Composable
fun CharacterStatusDeadPreview() {
    CharacterStatusComponent(characterStatus = CharacterStatus.Dead)
}

@Preview
@Composable
fun CharacterStatusUnknownPreview() {
    CharacterStatusComponent(characterStatus = CharacterStatus.Unknown)
}