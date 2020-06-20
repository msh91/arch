package io.github.msh91.arch.di.module

import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.github.msh91.arch.app.ArchApplication
import io.github.msh91.arch.data.source.db.AppDataBase
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(application: ArchApplication): AppDataBase {
        return Room
                .databaseBuilder(application, AppDataBase::class.java, AppDataBase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}