package io.github.msh91.arcyto.app

import android.app.Application
import io.github.msh91.arcyto.core.di.component.AppComponent
import io.github.msh91.arcyto.core.di.component.AppComponentProvider
import io.github.msh91.arcyto.di.DaggerAppMergeComponent

class ArcytoApp : Application(), AppComponentProvider {
    private lateinit var appComponent: AppComponent

    override fun get(): AppComponent = appComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppMergeComponent.builder()
            .application(this)
            .context(this)
            .build()
    }
}