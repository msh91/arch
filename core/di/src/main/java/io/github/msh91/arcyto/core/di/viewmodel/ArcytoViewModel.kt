package io.github.msh91.arcyto.core.di.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

val LocalViewModelProviderFactory = compositionLocalOf<ViewModelProvider.Factory> {
    error("ViewModelProviderFactory not found")
}

@Composable
inline fun <reified VM : ViewModel> arcytoViewModel(): VM = viewModel(factory = LocalViewModelProviderFactory.current)