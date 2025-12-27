package com.motorro.tasks.di

import com.motorro.core.coroutines.DispatcherProvider
import com.motorro.tasks.app.net.TasksApi
import com.motorro.tasks.app.repository.MemoryTaskStorage
import com.motorro.tasks.app.repository.ReadWriteTasks
import com.motorro.tasks.app.repository.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TasksModule {
    @Provides
    @Singleton
    fun tasksStorage(dispatcherProvider: DispatcherProvider, @App scope: CoroutineScope): ReadWriteTasks = MemoryTaskStorage(
        dispatcherProvider,
        scope
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
}