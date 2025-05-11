package com.motorro.cookbook.server.db

import com.motorro.cookbook.server.db.tables.RecipesTable
import com.motorro.cookbook.server.db.tables.UsersTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.exists
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun initDb(db: Database, initUsers: UsersTable.() -> Unit, initRecipes: RecipesTable.() -> Unit = {}) = transaction(db) {
    if (UsersTable.exists().not()) {
        SchemaUtils.create(UsersTable)
        UsersTable.initUsers()
    }
    if (RecipesTable.exists().not()) {
        SchemaUtils.create(RecipesTable)
        RecipesTable.initRecipes()
    }
}