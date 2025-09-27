package com.motorro.cookbook.addrecipe.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.appcore.navigation.CommonFlowHost
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
    protected lateinit var savedStateHandle: SavedStateHandle
    protected lateinit var stateMachine: CommonStateMachine<AddRecipeGesture, AddRecipeViewState>
    protected lateinit var factory: AddRecipeStateFactory

    protected lateinit var context: AddRecipeContext

    protected lateinit var nextState: AddRecipeState

    protected lateinit var flowHost: CommonFlowHost

    protected open fun createDispatcher(): TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun init() {
        dispatcher = createDispatcher()
        Dispatchers.setMain(dispatcher)
        savedStateHandle = SavedStateHandle()

        stateMachine = mockk(relaxed = true)
        factory = mockk()
        nextState = mockk()
        flowHost = mockk()

        context = object : AddRecipeContext {
            override val factory: AddRecipeStateFactory
                get() = this@BaseStateTest.factory
            override val savedStateHandle: SavedStateHandle
                get() = this@BaseStateTest.savedStateHandle
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