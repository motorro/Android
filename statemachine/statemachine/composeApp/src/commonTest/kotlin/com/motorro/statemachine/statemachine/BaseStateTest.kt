package com.motorro.statemachine.statemachine

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.statemachine.common.session.SessionManager
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.AppUiState
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseStateTest {

    lateinit var sessionManager: SessionManager
    lateinit var stateFactory: AppStateFactory
    lateinit var stateMachine: CommonStateMachine<AppGesture, AppUiState>

    protected lateinit var nextState: CommonMachineState<AppGesture, AppUiState>
    protected lateinit var dispatcher: TestDispatcher

    @BeforeTest
    fun init() {
        sessionManager = mock()
        stateFactory = mock()
        stateMachine = mock {
            every { this@mock.setUiState(any()) } returns Unit
            every { this@mock.setMachineState(any()) } returns Unit
        }

        nextState = object : CommonMachineState<AppGesture, AppUiState>() { }
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        doInit()
    }

    protected open fun doInit() = Unit

    @AfterTest
    fun deinit() {
        Dispatchers.resetMain()
    }

    protected fun test(testBody: suspend TestScope.() -> Unit) = runTest(dispatcher) {
        testBody()
    }
}