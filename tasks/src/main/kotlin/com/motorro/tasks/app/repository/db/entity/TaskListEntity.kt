package com.motorro.tasks.app.repository.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.motorro.tasks.data.UserName
import com.motorro.tasks.data.Version

/**
 * Common task-list data
 */
@Entity(tableName = "task_lists")
data class TaskListEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_name")
    val userName: UserName,
    @ColumnInfo(name = "latestVersion")
    val latestVersion: Version? = null
)