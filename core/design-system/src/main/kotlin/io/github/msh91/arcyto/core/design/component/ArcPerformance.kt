package io.github.msh91.arcyto.core.design.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.msh91.arcyto.core.design.R
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme

data class PerformanceValue(
    val text: String,
    val isPositive: Boolean,
)

@Composable
fun ArcPerformance(value: PerformanceValue, modifier: Modifier = Modifier) {
    val (color, icon) = when {
        value.isPositive -> Color.Green to R.drawable.ic_arrow_upward
        else -> Color.Red to R.drawable.ic_arrow_downward
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            tint = color,
            contentDescription = "Performance icon",
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = value.text,
            style = typography.labelSmall.copy(color = color),
            modifier = Modifier,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PerformancePreview() {
    ArcytoTheme {
        ArcPerformance(value = PerformanceValue("10%", true))
    }
}