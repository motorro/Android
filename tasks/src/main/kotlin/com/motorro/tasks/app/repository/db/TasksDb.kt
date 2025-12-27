package com.motorro.tasks.app.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.motorro.tasks.app.repository.db.entity.Converters
import com.motorro.tasks.app.repository.db.entity.TaskEntity
import com.motorro.tasks.app.repository.db.entity.TaskListEntity

@Database(
    entities = [
        TaskListEntity::class,
        TaskEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TasksDb : RoomDatabase() {
    /**
     * Tasks DAO
     */
    abstract fun tasksDao(): TasksDao
}