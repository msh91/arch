package io.github.msh91.arcyto.app

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import io.github.msh91.arcyto.di.AppGraph

class ArcytoApp : Application() {
    lateinit var appGraph: AppGraph
        private set

    override fun onCreate() {
        super.onCreate()
        appGraph =
            createGraphFactory<AppGraph.Factory>()
                .create(this)
    }
}

val Application.viewModelGraph: ViewModelGraph
    get() = (this as ArcytoApp).appGraph
