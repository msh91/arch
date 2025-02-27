package io.github.msh91.arcyto.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.github.msh91.arcyto.core.design.R
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme

/**
 * Displays a cryptocurrency icon from a URL using Coil.
 * Falls back to the built-in Bitcoin icon if no URL is provided or loading fails.
 */
@Composable
fun ArcCryptoIcon(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = "Cryptocurrency Icon",
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(56.dp)
                .background(color = colorScheme.primary, shape = CircleShape),
            error = rememberDefaultCryptoIcon(),
            fallback = rememberDefaultCryptoIcon(),
        )
    }
}

@Composable
private fun rememberDefaultCryptoIcon() = painterResource(id = R.drawable.ic_currency_bitcoin)

@Preview(showBackground = true)
@Composable
fun ArcCryptoIconPreview() {
    ArcytoTheme {
        ArcCryptoIcon(imageUrl = null)
    }
}