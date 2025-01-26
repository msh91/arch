package io.github.msh91.arcyto.di

import android.content.Context
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import io.github.msh91.arcyto.app.ArcytoApp
import io.github.msh91.arcyto.core.di.component.AppComponent
import io.github.msh91.arcyto.core.di.scope.AppScope
import javax.inject.Singleton

@Singleton
@MergeComponent(AppScope::class)
interface AppMergeComponent : AppComponent {
    fun inject(app: ArcytoApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: ArcytoApp): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppMergeComponent
    }
}