package com.motorro.tasks.app.repository

import com.motorro.core.lce.LceState
import com.motorro.tasks.TestDispatchers
import com.motorro.tasks.USER_NAME
import com.motorro.tasks.app.data.AppError
import com.motorro.tasks.app.net.TasksApi
import com.motorro.tasks.app.task1
import com.motorro.tasks.app.task2
import com.motorro.tasks.auth.data.SessionError
import com.motorro.tasks.data.ErrorCode
import com.motorro.tasks.data.HttpResponse
import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskCommand
import com.motorro.tasks.data.TaskUpdates
import com.motorro.tasks.data.Version
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.util.LinkedList
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class MemoryTasksRepositoryTest {
    private val api: TasksApi = mockk()
    private val dispatcher = UnconfinedTestDispatcher()

    private fun createRepository() = TasksRepository.Impl(
        api,
        MemoryTaskStorage(TestDispatchers(dispatcher))
    )

    @Test
    fun updatesTasks() = runTest(dispatcher) {
        val repository = createRepository()
        val newVersion = Version("ver1")
        coEvery { api.getUpdates(anyNullable()) } returns HttpResponse.Data(
            TaskUpdates(
                newVersion,
                listOf(task1, task2).map(TaskCommand::Upsert)
            )
        )

        val tasks = LinkedList<Collection<Task>>()
        val tCollector = launch {
            repository.getTasks(USER_NAME).collect(tasks::add)
        }

        val versions = LinkedList<Version?>()
        val vCollector = launch {
            repository.getVersion(USER_NAME).collect(versions::add)
        }

        assertContentEquals(
            listOf<LceState<Unit, AppError>>(
                LceState.Loading(Unit),
                LceState.Content(Unit)
            ),
            repository.update(USER_NAME).toList()
        )

        tCollector.cancelAndJoin()
        assertEquals(
            listOf(emptyList(), listOf(task1, task2)),
            tasks.map { it.toList() }
        )
        vCollector.cancelAndJoin()
        assertEquals(
            listOf(null, newVersion),
            versions
        )

        coVerify {
            api.getUpdates(null)
        }
    }

    @Test
    fun failsIfUpdateFails() = runTest(dispatcher) {
        val repository = createRepository()
        coEvery { api.getUpdates(anyNullable()) } returns HttpResponse.Error(
            ErrorCode.FORBIDDEN,
            ErrorCode.FORBIDDEN.defaultMessage
        )

        val tasks = LinkedList<Collection<Task>>()
        val tCollector = launch {
            repository.getTasks(USER_NAME).collect(tasks::add)
        }

        val versions = LinkedList<Version?>()
        val vCollector = launch {
            repository.getVersion(USER_NAME).collect(versions::add)
        }

        val (first, second) = repository.update(USER_NAME).toList()
        assertIs<LceState.Loading<*>>(first)
        assertIs<LceState.Error<*, AppError>>(second)
        assertIs<AppError.Authentication>(second.error)
        assertIs<SessionError.Authentication>(second.error.cause)

        tCollector.cancelAndJoin()
        assertEquals(
            listOf(emptyList()),
            tasks.map { it.toList() }
        )
        vCollector.cancelAndJoin()
        assertEquals<List<Version?>>(
            listOf(null),
            versions
        )

        coVerify {
            api.getUpdates(null)
        }
    }
}