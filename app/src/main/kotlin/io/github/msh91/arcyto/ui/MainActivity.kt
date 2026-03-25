package io.github.msh91.arcyto.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.msh91.arcyto.app.viewModelGraph
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArcytoTheme {
                ArcytoAppScreen(application.viewModelGraph.metroViewModelFactory)
            }
        }
    }
}
