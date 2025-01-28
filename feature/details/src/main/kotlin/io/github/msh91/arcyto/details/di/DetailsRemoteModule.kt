package io.github.msh91.arcyto.details.di

import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import io.github.msh91.arcyto.core.di.scope.AppScope
import io.github.msh91.arcyto.details.data.remote.CoinDetailsDataSource
import retrofit2.Retrofit
import retrofit2.create

@Module
@ContributesTo(AppScope::class)
class DetailsRemoteModule {
    @Provides
    fun provideDetailsRemoteDataSource(retrofit: Retrofit): CoinDetailsDataSource {
        return retrofit.create()
    }
}