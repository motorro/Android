package com.motorro.cookbook.app

import android.app.Application
import com.motorro.cookbook.app.repository.MockRepository
import com.motorro.cookbook.app.repository.RecipeRepository
import com.motorro.cookbook.app.session.MemorySessionStorage
import com.motorro.cookbook.app.session.MockUserApi
import com.motorro.cookbook.app.session.SessionManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

/**
 * The application. Provides access to dependencies.
 */
@OptIn(DelicateCoroutinesApi::class)
class App : Application() {
    /**
     * Session manager
     */
    val sessionManager: SessionManager by lazy {
        SessionManager.Impl(MemorySessionStorage(), MockUserApi(), GlobalScope)
    }

    /**
     * The recipe repository.
     */
    val recipeRepository: RecipeRepository by lazy {
        MockRepository(sessionManager)
    }
}