package com.taild.jetcartoon.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.taild.jetcartoon.ui.screens.charaterdetail.LoadingState

@Composable
fun CharacterImage(url: String) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = "Character image",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp)),
        loading = {
            LoadingState()
        }
    )
}