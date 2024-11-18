package com.taild.jetcartoon.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.taild.domain.Character
import com.taild.jetcartoon.ui.screens.charaterdetail.DataPoint
import com.taild.jetcartoon.ui.theme.RickAction

@Composable
fun CharacterListItem(
    modifier: Modifier = Modifier,
    character: Character,
    characterDataPoints: List<DataPoint>,
    onClick: () -> Unit
) {
    Row (
        modifier = modifier
            .height(140.dp)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(Color.Transparent, RickAction)),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
    ) {
        Box {
            CharacterImage(
                url = character.imageUrl,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
            )
            CharacterCircleStatus(
                status = character.status,
                modifier = Modifier.padding(top = 10.dp, start = 10.dp))
        }

        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                items = listOf(DataPoint(title = "Name", description = character.name)) + characterDataPoints,
                key = { item -> item.title }
            ) {dataPoint ->
                DataPointComponent(dataPoint = shortenDataPoint(dataPoint))
            }
        }
    }
}

private fun shortenDataPoint(dataPoint: DataPoint): DataPoint {
    val newDes = if (dataPoint.description.length > 14) {
        dataPoint.description.take(12) + ".."
    } else {
        dataPoint.description
    }

    return dataPoint.copy(description = newDes)
}