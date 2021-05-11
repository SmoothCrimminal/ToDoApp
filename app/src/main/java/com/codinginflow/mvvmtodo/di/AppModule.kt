package com.codinginflow.mvvmtodo.di

import android.app.Application
import androidx.room.Room
import com.codinginflow.mvvmtodo.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ) =  Room.databaseBuilder(app, TaskDatabase::class.java, "TaskDB")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

}