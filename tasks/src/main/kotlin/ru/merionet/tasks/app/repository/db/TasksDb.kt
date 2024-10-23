package ru.merionet.tasks.app.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.merionet.tasks.app.repository.db.entity.Converters
import ru.merionet.tasks.app.repository.db.entity.TaskEntity
import ru.merionet.tasks.app.repository.db.entity.TaskListEntity

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