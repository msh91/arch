package io.github.msh91.arcyto.core.design.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme

@Composable
fun ArcDetailsListLoading(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize().padding(all = 16.dp)
    ) {

        Card(
            elevation = cardElevation(defaultElevation = 0.dp),
            colors = cardColors(containerColor = colorScheme.background),
            modifier = modifier
                .width(100.dp)
                .height(34.dp),
        ) {
            ShimmerEffect(modifier = Modifier.fillMaxSize())
        }
        Card(
            elevation = cardElevation(defaultElevation = 0.dp),
            colors = cardColors(containerColor = colorScheme.background),
            modifier = modifier
                .width(120.dp)
                .height(34.dp),
        ) {
            ShimmerEffect(modifier = Modifier.fillMaxSize())
        }
        Card(
            elevation = cardElevation(defaultElevation = 0.dp),
            colors = cardColors(containerColor = colorScheme.background),
            modifier = modifier
                .fillMaxWidth()
                .height(34.dp),
        ) {
            ShimmerEffect(modifier = Modifier.fillMaxSize())
        }
        Card(
            elevation = cardElevation(defaultElevation = 0.dp),
            colors = cardColors(containerColor = colorScheme.background),
            modifier = modifier
                .width(150.dp)
                .height(200.dp),
        ) {
            ShimmerEffect(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArcDetailsListLoadingPreview() {
    ArcytoTheme {
        ArcDetailsListLoading()
    }
}