package com.motorro.tasks.di

import android.content.Context
import androidx.room.Room
import com.motorro.core.coroutines.DispatcherProvider
import com.motorro.tasks.app.net.TasksApi
import com.motorro.tasks.app.repository.DbTaskStorage
import com.motorro.tasks.app.repository.ReadWriteTasks
import com.motorro.tasks.app.repository.TasksRepository
import com.motorro.tasks.app.repository.db.TasksDao
import com.motorro.tasks.app.repository.db.TasksDb
import com.motorro.tasks.app.work.TaskUpdateWorker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TasksModule {
    @Provides
    @Singleton
    fun tasksDb(@ApplicationContext context: Context): TasksDb = Room
        .databaseBuilder(context, TasksDb::class.java, "tasks.db")
        .build()

    @Provides
    @Singleton
    fun tasksDao(db: TasksDb): TasksDao = db.tasksDao()

    @Provides
    @Singleton
    fun tasksStorage(
        tasksDao: TasksDao,
        dispatchers: DispatcherProvider,
    ): ReadWriteTasks = DbTaskStorage(
        tasksDao,
        dispatchers
    )
}

@Module
@InstallIn(SingletonComponent::class)
interface TasksBindingModule {
    @Binds
    @Singleton
    fun tasksApi(impl: TasksApi.Impl): TasksApi

    @Binds
    @Singleton
    fun tasksRepository(impl: TasksRepository.Impl): TasksRepository

    @Binds
    @Singleton
    fun updateScheduler(impl: TaskUpdateWorker.Scheduler.Impl): TaskUpdateWorker.Scheduler
}