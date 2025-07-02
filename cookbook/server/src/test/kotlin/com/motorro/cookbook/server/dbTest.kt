package com.motorro.cookbook.server

import com.motorro.cookbook.model.UserId
import com.motorro.cookbook.server.db.initDb
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

@OptIn(ExperimentalCoroutinesApi::class)
fun dbTest(test: suspend Transaction.(UserId) -> Unit) = runTest(UnconfinedTestDispatcher()) {
    val db = Database.connect(
        "jdbc:sqlite:file:test?mode=memory&cache=shared",
        "org.sqlite.JDBC"
    )
    TransactionManager.defaultDatabase = db

    transaction {
        lateinit var userId: EntityID<Int>

        initDb(db, initUsers = {
            userId = insertAndGetId {
                it[name] = "USER"
                it[password] = "PASSWORD"
            }
        })

        runBlocking { test(UserId(userId.value)) }
    }
}