package com.motorro.cookbook.recipe.state

import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.cookbook.appcore.navigation.CommonFlowHost
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
internal abstract class BaseStateTest {

    protected lateinit var dispatcher: TestDispatcher
    protected lateinit var stateMachine: CommonStateMachine<RecipeGesture, RecipeViewState>
    protected lateinit var factory: RecipeStateFactory

    protected lateinit var context: RecipeContext

    protected lateinit var nextState: RecipeState

    protected lateinit var flowHost: CommonFlowHost

    protected open fun createDispatcher(): TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun init() {
        dispatcher = createDispatcher()
        Dispatchers.setMain(dispatcher)

        stateMachine = mockk(relaxed = true)
        factory = mockk()
        nextState = mockk()

        context = object : RecipeContext {
            override val factory: RecipeStateFactory
                get() = this@BaseStateTest.factory
            override val flowHost: CommonFlowHost
                get() = this@BaseStateTest.flowHost
        }

        doInit()
    }

    protected open fun doInit() { }

    @After
    fun deinit() {
        Dispatchers.resetMain()
    }

    protected inline fun test(crossinline block: suspend TestScope.() -> Unit) = runTest(dispatcher) { block() }
}