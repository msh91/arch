package io.github.msh91.arch.ui.home

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.msh91.arch.ui.home.chart.CryptoChartFragment
import io.github.msh91.arch.ui.home.list.LatestUpdatesFragment

/**
 * All fragments of [HomeActivity] should be provided via this [Module]
 */
@Module
abstract class HomeFragmentProvider {

    @ContributesAndroidInjector
    abstract fun provideLatestUpdatesFragment(): LatestUpdatesFragment

    @ContributesAndroidInjector
    abstract fun provideCryptoChartFragment(): CryptoChartFragment
}
