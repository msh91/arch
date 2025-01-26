package io.github.msh91.arcyto.core.design.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
fun CardListLoading(modifier: Modifier = Modifier, count: Int = 5) {
    LazyColumn(modifier) {
        items(count) {
            LoadingItem()
        }
    }
}

@Composable
private fun LoadingItem(
    modifier: Modifier = Modifier,
) {
    Card(
        elevation = cardElevation(defaultElevation = 2.dp, pressedElevation = 8.dp),
        colors = cardColors(containerColor = colorScheme.background),
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            ShimmerEffect(
                modifier = Modifier
                    .width(120.dp)
                    .height(32.dp)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
            ShimmerEffect(
                modifier = Modifier
                    .width(240.dp)
                    .height(32.dp)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CardListLoadingPreview() {
    ArcytoTheme {
        CardListLoading()
    }
}