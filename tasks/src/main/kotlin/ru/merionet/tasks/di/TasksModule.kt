package ru.merionet.tasks.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import ru.merionet.core.coroutines.DispatcherProvider
import ru.merionet.tasks.app.net.TasksApi
import ru.merionet.tasks.app.repository.MemoryTaskStorage
import ru.merionet.tasks.app.repository.ReadWriteTasks
import ru.merionet.tasks.app.repository.TasksRepository
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