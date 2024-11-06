package com.taild.jetcartoon.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.taild.jetcartoon.ui.theme.RickAction

@Composable
fun CharacterNameComponent(name: String) {
    Text(
        text = name,
        fontSize = 42.sp,
        fontWeight = FontWeight.Bold,
        color = RickAction
    )
}