package io.github.msh91.arch.di.module

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import io.github.msh91.arch.data.source.db.AppDataBase
import io.github.msh91.arch.data.source.db.dao.MovieDao
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(application: Application): AppDataBase {
        return Room
                .databaseBuilder(application, AppDataBase::class.java, AppDataBase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    fun provideMovieDao(appDataBase: AppDataBase): MovieDao {
        return appDataBase.movieDao()
    }
}