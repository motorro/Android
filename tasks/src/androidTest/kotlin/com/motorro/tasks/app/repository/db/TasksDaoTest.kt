package com.motorro.tasks.app.repository.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.motorro.tasks.USER_NAME
import com.motorro.tasks.app.repository.db.entity.TaskEntity
import com.motorro.tasks.app.repository.db.entity.toTask
import com.motorro.tasks.app.task1
import com.motorro.tasks.app.task2
import com.motorro.tasks.data.TaskCommand
import com.motorro.tasks.data.TaskUpdates
import com.motorro.tasks.data.Version
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.LinkedList
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TasksDaoTest {
    private lateinit var db: TasksDb
    private lateinit var tasksDao: TasksDao
    private val dispatcher = UnconfinedTestDispatcher()


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TasksDb::class.java).build()
        tasksDao = db.tasksDao()
    }

    @After
    fun shutdown() {
        db.close()
    }

    @Test
    fun returnsEmptyTaskListWhenRecordsNotFound() = runTest(dispatcher) {
        val versions = tasksDao.getVersion(USER_NAME).take(1).toList().first()
        val tasks = tasksDao.getTasks(USER_NAME).take(1).toList().first()
        assertTrue {
            versions.isEmpty()
        }
        assertTrue {
            tasks.isEmpty()
        }
    }

    @Test
    fun updatesTasks() = runTest(dispatcher) {
        val version = Version("version1")
        val versions = LinkedList<List<Version>>()
        val tasks = LinkedList<List<TaskEntity>>()
        val versionsCollector = backgroundScope.launch {
            tasksDao.getVersion(USER_NAME).take(2).collect(versions::add)
        }
        val taskCollector = backgroundScope.launch {
            tasksDao.getTasks(USER_NAME).take(2).collect(tasks::add)
        }

        tasksDao.update(
            USER_NAME,
            TaskUpdates(
                version,
                listOf(
                    TaskCommand.Upsert(task1),
                    TaskCommand.Upsert(task2),
                    TaskCommand.Delete(task2.id)
                )
            )
        )

        joinAll(versionsCollector, taskCollector)

        assertEquals(
            listOf(emptyList(), listOf(version)),
            versions
        )
        assertEquals(
            listOf(emptyList(), listOf(task1)),
            tasks.map { list -> list.map { it.toTask() } }
        )
    }
}