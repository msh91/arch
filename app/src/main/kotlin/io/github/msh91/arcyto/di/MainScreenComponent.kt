package io.github.msh91.arcyto.di

import android.content.Context
import com.squareup.anvil.annotations.ContributesSubcomponent
import com.squareup.anvil.annotations.ContributesTo
import io.github.msh91.arcyto.ui.MainActivity
import io.github.msh91.arcyto.core.di.component.AppComponentProvider
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.core.di.scope.MainScreenScope
import io.github.msh91.arcyto.core.di.scope.SingleIn

@SingleIn(MainScreenScope::class)
@ContributesSubcomponent(
    scope = MainScreenScope::class,
    parentScope = AppScope::class,
)
interface MainScreenComponent {
    fun inject(mainActivity: MainActivity)

    @ContributesSubcomponent.Factory
    interface Factory {
        fun create(): MainScreenComponent
    }

    @ContributesTo(AppScope::class)
    interface ParentComponent {
        fun getMainScreenComponentFactory(): Factory
    }
}

fun Context.getMainScreenComponentFactory(): MainScreenComponent.Factory {
    val appComponentProvider = applicationContext as AppComponentProvider
    val parentComponent = appComponentProvider.get() as MainScreenComponent.ParentComponent
    return parentComponent.getMainScreenComponentFactory()
}