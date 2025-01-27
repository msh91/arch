package io.github.msh91.arcyto.core.design.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.msh91.arcyto.core.design.R
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme
import io.github.msh91.arcyto.core.design.theme.White

@Composable
fun ArcBitcoinIcon(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.ic_currency_bitcoin),
            colorFilter = tint(White),
            contentDescription = "Bitcoin Icon",
            modifier = Modifier
                .background(color = colorScheme.primary, shape = CircleShape)
                .padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BitcoinIconPreview() {
    ArcytoTheme {
        ArcBitcoinIcon()
    }
}