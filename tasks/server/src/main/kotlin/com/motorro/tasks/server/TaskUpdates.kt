package com.motorro.tasks.server

import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskCommand
import com.motorro.tasks.data.TaskUpdateRequest
import com.motorro.tasks.data.TaskUpdates
import com.motorro.tasks.data.Version
import com.motorro.tasks.data.VersionResponse
import com.motorro.tasks.data.nextVersion
import java.util.Deque
import java.util.LinkedList
import java.util.concurrent.locks.ReentrantReadWriteLock

class TaskUpdates(toCreate: Sequence<Task>) {
    private val lock = ReentrantReadWriteLock()
    private val read = lock.readLock()
    private val write = lock.writeLock()

    private val syncList: Deque<SyncEntry>

    init {
        syncList = LinkedList(listOf(
            SyncEntry(
                version = nextVersion(),
                commands = toCreate.map(TaskCommand::Upsert).toList()
            )
        ))
    }

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
