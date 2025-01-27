package io.github.msh91.arcyto.core.design.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
fun ArcHistoricalListLoading(modifier: Modifier = Modifier, count: Int = 8) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier) {
        items(count, key = { it }) {
            LoadingItem()
        }
    }
}

@Composable
private fun LoadingItem(
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
        colors = cardColors(containerColor = colorScheme.background),
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 8.dp),
    ) {
        ShimmerEffect(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardListLoadingPreview() {
    ArcytoTheme {
        ArcHistoricalListLoading()
    }
}