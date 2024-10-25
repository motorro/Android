package ru.merionet.tasks.server

import io.ktor.util.logging.KtorSimpleLogger
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.TaskCommand
import ru.merionet.tasks.data.TaskUpdateRequest
import ru.merionet.tasks.data.TaskUpdates
import ru.merionet.tasks.data.Version
import ru.merionet.tasks.data.VersionResponse
import ru.merionet.tasks.data.nextVersion
import java.util.Deque
import java.util.LinkedList
import java.util.concurrent.locks.ReentrantReadWriteLock

internal val LOGGER = KtorSimpleLogger("ru.merionet.tasks.server.TaskUpdates")

class TaskUpdates(toCreate: Sequence<Task>) {
    private val lock = ReentrantReadWriteLock()
    private val read = lock.readLock()
    private val write = lock.writeLock()

    private val syncList: Deque<SyncEntry> = LinkedList(listOf(
        SyncEntry(
            version = nextVersion(),
            commands = toCreate.map(TaskCommand::Upsert).toList()
        )
    ))

    /**
     * returns current version
     */
    fun getCurrentVersion(): VersionResponse {
        read.lock()
        try {
            return VersionResponse(syncList.last.version)
        } finally {
            read.unlock()
        }
    }

    /**
     * Get updates
     */
    fun get(latestVersion: Version? = null): TaskUpdates {
        read.lock()
        try {
            val commands = LinkedList<List<TaskCommand>>()
            var version: Version? = null

            val di = syncList.descendingIterator()
            while (di.hasNext()) {
                val entry = di.next()
                if (null == version) {
                    version = entry.version
                }
                if (latestVersion == entry.version) {
                    break
                }
                commands.addFirst(entry.commands)
            }

            return TaskUpdates(
                latestVersion = version ?: getCurrentVersion().latestVersion,
                commands = commands.flatten()
            )
        } finally {
            read.unlock()
        }
    }

    /**
     * Register updates
     */
    fun register(request: TaskUpdateRequest): VersionResponse {
        if (request.commands.isEmpty()) {
            return getCurrentVersion()
        }

        LOGGER.info("Updating tasks: $request")

        write.lock()
        try {
            if (getCurrentVersion().latestVersion != request.expectedVersion) {
                throw IllegalStateException("Version conflict. Please update your list first!")
            }
            val next = request.nextVersion
            syncList.add(SyncEntry(next, request.commands))
            return VersionResponse(next)
        } finally {
            write.unlock()
        }
    }
}

/**
 * Synchronization entry
 */
private class SyncEntry(
    val version: Version,
    val commands: List<TaskCommand>
)
