package com.motorro.cookbook.server.db.tables

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.ImmutableCachedEntityClass
import org.jetbrains.exposed.v1.dao.IntEntity

const val MAX_CREDENTIALS_LENGTH: Int = 128

const val USERS_TABLE_NAME = "users"
const val USERS_ID = "user_id"

/**
 * Users table
 */
object UsersTable : IntIdTable(USERS_TABLE_NAME, USERS_ID) {
    val name = varchar("name", MAX_CREDENTIALS_LENGTH).uniqueIndex()
    val password = varchar("password", MAX_CREDENTIALS_LENGTH)
}

/**
 * User
 */
class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    val name by UsersTable.name
    val password by UsersTable.password

    companion object : ImmutableCachedEntityClass<Int, UserEntity>(UsersTable)
}
