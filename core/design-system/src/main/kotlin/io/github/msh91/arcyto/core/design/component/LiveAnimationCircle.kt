package io.github.msh91.arcyto.core.design.component

import android.content.res.Configuration
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme

@Composable
fun LiveAnimationCircle(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()

    val circleRadius by infiniteTransition.animateFloat(
        initialValue = 2f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val color by infiniteTransition.animateColor(
        initialValue = colorScheme.secondary.copy(alpha = 0.1f),
        targetValue = colorScheme.secondary,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    CircleAnimation(circleRadius, color, modifier)
}

@Composable
fun CircleAnimation(circleRadius: Float, color: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.size(24.dp)) {
            drawCircle(
                color = color,
                radius = circleRadius,
                center = Offset(size.width / 2, size.height / 2)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LiveAnimationCirclePreview() {
    ArcytoTheme {
        LiveAnimationCircle()
    }
}
