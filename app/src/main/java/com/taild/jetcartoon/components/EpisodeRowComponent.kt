package com.taild.jetcartoon.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taild.domain.Episode
import com.taild.jetcartoon.ui.theme.RickTextPrimary

@Composable
fun EpisodeRowComponent(episode: Episode) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = "Episode",
                color = RickTextPrimary
            )
            Text(
                text = episode.episodeNumber.toString(),
                fontSize = 26.sp,
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = episode.name,
                fontSize = 26.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.width(300.dp)
            )
            Text(
                text = episode.airDate,
                color = RickTextPrimary,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
            )
        }
    }
}