package io.github.msh91.arch.app

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.github.msh91.arch.di.component.DaggerAppComponent

/**
 * Custom [Application] class for app that prepare app for running
 */
class ArchApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)
}
