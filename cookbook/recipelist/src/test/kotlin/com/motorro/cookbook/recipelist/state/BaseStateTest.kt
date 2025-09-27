package com.motorro.cookbook.recipelist.state

import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
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
    protected lateinit var stateMachine: CommonStateMachine<RecipeListGesture, RecipeListViewState>
    protected lateinit var factory: RecipeListStateFactory

    protected lateinit var context: RecipeListContext

    protected lateinit var nextState: RecipeListState

    protected open fun createDispatcher(): TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun init() {
        dispatcher = createDispatcher()
        Dispatchers.setMain(dispatcher)

        stateMachine = mockk(relaxed = true)
        factory = mockk()
        nextState = mockk()

        context = object : RecipeListContext {
            override val factory: RecipeListStateFactory
                get() = this@BaseStateTest.factory
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