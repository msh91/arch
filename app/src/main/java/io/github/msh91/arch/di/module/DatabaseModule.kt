package io.github.msh91.arch.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.github.msh91.arch.data.source.db.AppDataBase
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context): AppDataBase {
        return Room
            .databaseBuilder(context, AppDataBase::class.java, AppDataBase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
