package io.github.msh91.arcyto.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import io.github.msh91.arcyto.core.design.theme.ArcytoTheme
import io.github.msh91.arcyto.di.getMainScreenComponentFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationContext.getMainScreenComponentFactory()
            .create()
            .inject(this)
        enableEdgeToEdge()
        setContent {
            ArcytoTheme {
                ArcytoAppScreen(viewModelProviderFactory)
            }
        }
    }
}
