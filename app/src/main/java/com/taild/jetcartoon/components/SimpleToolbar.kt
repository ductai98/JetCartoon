package com.taild.jetcartoon.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taild.jetcartoon.ui.theme.RickPrimary
import com.taild.jetcartoon.ui.theme.RickTextPrimary

@Composable
fun SimpleToolbar(
    title: String,
    onBackAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.background(color = RickPrimary)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (onBackAction != null) {
                Box(
                    modifier = Modifier
                        .clickable { onBackAction() }
                        .background(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Transparent
                        )
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
            Text(
                text = title,
                fontSize = 30.sp,
                style = TextStyle(
                    color = RickTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        HorizontalDivider(thickness = 1.dp, color = RickTextPrimary)
    }
}