package com.taild.jetcartoon.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taild.domain.Character
import com.taild.domain.CharacterGender
import com.taild.domain.CharacterStatus
import com.taild.jetcartoon.ui.theme.RickAction

@Composable
fun CharacterGridItem(
    modifier: Modifier = Modifier,
    character: Character,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(Color.Transparent, RickAction)),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Box {
            CharacterImage(
                url = character.imageUrl
            )
            CharacterCircleStatus(
                status = character.status,
                modifier = Modifier.padding(top = 10.dp, start = 10.dp))
        }
        Text(
            text = character.name,
            color = RickAction,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            lineHeight = 26.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .height(60.dp)
        )
    }
}


@Preview
@Composable
private fun CharacterGridItemPreview() {
    CharacterGridItem(character = fakeCharacter, onClick = {})
}

val fakeCharacter: Character = Character(
    id = 5,
    name = "Jerry Smith",
    status = CharacterStatus.Alive,
    species = "Human",
    type = "",
    gender = CharacterGender.Male,
    origin = Character.Origin(
        name = "Earth (C-137)",
        url = "https://rickandmortyapi.com/api/location/20"
    ),
    location = Character.Location(
        name = "Earth",
        url = "https://rickandmortyapi.com/api/location/20"
    ),
    imageUrl = "https://rickandmortyapi.com/api/character/avatar/5.jpeg",
    episodeIds = emptyList(),
    url = "https://rickandmortyapi.com/api/character/5",
    created = "2017-11-04T19:22:43.665Z"
)