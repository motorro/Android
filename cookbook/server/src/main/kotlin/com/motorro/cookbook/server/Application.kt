package com.motorro.cookbook.server

import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.UserId
import com.motorro.cookbook.server.db.initDb
import com.motorro.cookbook.server.db.tables.RecipesTable
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticFiles
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

private const val USER_AREA = "user-area"
private object RecipesDb {
    val db: Database by lazy {
        Database.connect(
            url = "jdbc:sqlite:$DB_PATH",
            driver = "org.sqlite.JDBC"
        )
    }
}

fun main() {
    DATA_PATH.toFile().mkdirs()
    TransactionManager.defaultDatabase = RecipesDb.db
    lateinit var userId: EntityID<Int>
    initDb(
        RecipesDb.db,
        initUsers =  {
            userId = insertAndGetId {
                it[name] = USER
                it[password] = PASSWORD
            }
        },
        initRecipes = {
            sampleRecipes.forEach { recipe ->
                insert {
                    it[RecipesTable.userId] = userId
                    it[recipeId] = recipe.id.toJavaUuid()
                    it[title] = recipe.title
                    it[category] = recipe.category.name
                    it[imagePath] = null
                    it[image] = recipe.image?.toString()
                    it[description] = recipe.description
                    it[dateTimeCreated] = recipe.dateTimeCreated
                }
            }
        }
    )

    val user: User = User.Impl()

    embeddedServer(
        Netty,
        host = SERVER_HOST,
        port = SERVER_PORT,
        module = {
            module(
                recipes = Recipes.Impl(IMAGE_PATH, IMAGE_BASE),
                user = user,
                delay = DELAY
            )
        }
    ).start(wait = true)
}


fun Application.module(recipes: Recipes, user: User, delay: Long = 0) {

    suspend fun <R> delay(block: suspend () -> R): R {
        if (delay > 0) {
            kotlinx.coroutines.delay(delay)
        }
        return block()
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.message ?: "Bad request")
        }
        exception<IllegalStateException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, cause.message ?: "Conflict")
        }
        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
        }
        status(HttpStatusCode.Forbidden) { call, _ ->
            call.respond(HttpStatusCode.Forbidden, "Forbidden")
        }
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.message ?: "Not found")
        }
        exception<RuntimeException> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: "Internal server error")
        }
        unhandled { call ->
            call.respond(HttpStatusCode.NotFound, "Not found")
        }
    }
    install(Authentication) {
        user.auth(this, USER_AREA)
    }
    routing {
        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml")
        staticFiles("/images", IMAGE_PATH.toFile())

        authenticate(USER_AREA) {

            get("/profile") {
                delay { call.respond(user.getProfile(call.userId())) }
            }

            get("/recipes") {
                delay { call.respond(recipes.getAll(call.userId())) }
            }

            get("/recipes/{id}") {
                delay { call.respond(recipes.get(call.requireRecipeId())) }
            }

            post("/recipes") {
                delay { call.respond(recipes.add(call.userId(), call.receive<Recipe>())) }
            }

            put("/recipes/{id}/image") {
                delay {
                    call.respond(call.withFileStream("image") { name, contentType, streamProvider ->
                        recipes.setImage(call.requireRecipeId(), name, contentType, streamProvider)
                    })
                }
            }

            delete("recipes/{id}") {
                delay {
                    recipes.delete(call.requireRecipeId())
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}

private fun ApplicationCall.userId(): UserId {
    val principal = checkNotNull(principal<UserIdPrincipal>()) { "Should be authenticated!" }
    return principal.id
}

private fun ApplicationCall.requireRecipeId(): RecipeId = RecipeId(
    userId = userId(),
    recipeId = parameters["id"]?.let(Uuid::parse) ?: throw IllegalArgumentException("Invalid recipe id")
)

private suspend inline fun <R> ApplicationCall.withFileStream(name: String?, block: (String?, ContentType?, () -> ByteReadChannel) -> R): R {
    val multipart = receiveMultipart()
    while (true) {
        val partResult = runCatching { multipart.readPart() }.onFailure {
            currentCoroutineContext().ensureActive()
            throw RuntimeException("Error reading request", it)
        }
        val part = partResult.getOrThrow() ?: throw IllegalArgumentException("Part $name was not found in the request")
        if (part is PartData.FileItem && name == part.name) {
            return block(part.originalFileName, part.contentType, part.provider)
        }
    }
}
