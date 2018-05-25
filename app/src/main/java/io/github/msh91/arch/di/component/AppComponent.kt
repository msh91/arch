package io.github.msh91.arch.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import io.github.msh91.arch.app.ArchApplication
import io.github.msh91.arch.di.builder.ActivityBuilder
import io.github.msh91.arch.di.module.AppModule
import io.github.msh91.arch.di.module.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AppModule::class),
    (NetworkModule::class),
    (AndroidInjectionModule::class),
    (ActivityBuilder::class)])
interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }

    fun inject(app: ArchApplication)
}
