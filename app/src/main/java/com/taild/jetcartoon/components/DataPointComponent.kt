package com.taild.jetcartoon.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.taild.jetcartoon.ui.screens.charaterdetail.DataPoint
import com.taild.jetcartoon.ui.theme.RickAction
import com.taild.jetcartoon.ui.theme.RickTextPrimary

@Composable
fun DataPointComponent(dataPoint: DataPoint) {
    Column {
        Text(
            text = dataPoint.title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = RickAction
        )
        Text(
            text = dataPoint.description,
            fontSize = 24.sp,
            color = RickTextPrimary
        )
    }
}

@Preview
@Composable
fun DataPointComponentPreview() {
    MaterialTheme {
        Surface(
            color = Color.Black
        ) {
            DataPointComponent(dataPoint = DataPoint("Last known location", "Earth (C-137)"))
        }
    }
}