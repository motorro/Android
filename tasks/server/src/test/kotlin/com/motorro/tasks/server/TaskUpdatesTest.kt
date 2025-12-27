package com.motorro.tasks.server

import com.motorro.tasks.data.TaskCommand
import com.motorro.tasks.data.TaskUpdateRequest
import com.motorro.tasks.data.nextVersion
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TaskUpdatesTest {
    @Test
    fun newTasksHasNoUpdatesIfNotSet() {
        val updates = TaskUpdates(emptySequence())
        val currentUpdates = updates.get(null)
        assertTrue { currentUpdates.commands.isEmpty() }
    }

    @Test
    fun newTasksHasCreationUpdatesIfPopulated() {
        val task = createTask()
        val updates = TaskUpdates(sequenceOf(task))
        val currentUpdates = updates.get(null)
        assertEquals(
            listOf(TaskCommand.Upsert(task)),
            currentUpdates.commands
        )
    }

    @Test
    fun addsNewUpdates() {
        val task1 = createTask()
        val task2 = createTask()
        val updates = TaskUpdates(sequenceOf(task1))

        val versionBefore = updates.getCurrentVersion()
        val nextVersion = nextVersion()

        val versionAfter = updates.register(TaskUpdateRequest(
            versionBefore.latestVersion,
            nextVersion,
            listOf(TaskCommand.Upsert(task2))
        ))

        assertEquals(nextVersion, versionAfter.latestVersion)

        val updatesAfter = updates.get()
        assertEquals(versionAfter.latestVersion, updatesAfter.latestVersion)
        assertEquals(
            listOf(
                TaskCommand.Upsert(task1),
                TaskCommand.Upsert(task2)
            ),
            updatesAfter.commands
        )
    }

    @Test
    fun mergesUpdateVersions() {
        val task1 = createTask()
        val task2 = createTask()
        val task3 = createTask()
        val updates = TaskUpdates(sequenceOf(task1))

        val version1 = updates.getCurrentVersion()
        val version2 = updates.register(TaskUpdateRequest(
            version1.latestVersion,
            nextVersion(),
            listOf(TaskCommand.Upsert(task2))
        ))
        val version3 = updates.register(TaskUpdateRequest(
            version2.latestVersion,
            nextVersion(),
            listOf(TaskCommand.Upsert(task3))
        ))

        val allUpdates = updates.get()
        assertEquals(version3.latestVersion, allUpdates.latestVersion)
        assertEquals(
            listOf(task1, task2, task3).map(TaskCommand::Upsert),
            allUpdates.commands
        )
    }

    @Test
    fun returnsUpdatesWithRespectToVersions() {
        val task1 = createTask()
        val task2 = createTask()
        val task3 = createTask()
        val updates = TaskUpdates(sequenceOf(task1))

        val version1 = updates.getCurrentVersion()
        val version2 = updates.register(TaskUpdateRequest(
            version1.latestVersion,
            nextVersion(),
            listOf(TaskCommand.Upsert(task2))
        ))
        val version3 = updates.register(TaskUpdateRequest(
            version2.latestVersion,
            nextVersion(),
            listOf(TaskCommand.Upsert(task3))
        ))

        val updatesSinceVersion3 = updates.get(version3.latestVersion)
        assertEquals(version3.latestVersion, updatesSinceVersion3.latestVersion)
        assertTrue { updatesSinceVersion3.commands.isEmpty() }

        val updatesSinceVersion2 = updates.get(version2.latestVersion)
        assertEquals(version3.latestVersion, updatesSinceVersion2.latestVersion)
        assertEquals(
            listOf(task3).map(TaskCommand::Upsert),
            updatesSinceVersion2.commands
        )

        val updatesSinceVersion1 = updates.get(version1.latestVersion)
        assertEquals(version3.latestVersion, updatesSinceVersion1.latestVersion)
        assertEquals(
            listOf(task2, task3).map(TaskCommand::Upsert),
            updatesSinceVersion1.commands
        )
    }
}