package com.google.jetstream.presentation.screens.series

import androidx.compose.foundation.clickable
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.google.jetstream.data.models.SeriesSingle

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SDetailsScreen(
    detail: String?
){
    Text(
        text = "Series $detail",
        style = MaterialTheme.typography.h1,
        modifier = Modifier.clickable {

        }
    )
}