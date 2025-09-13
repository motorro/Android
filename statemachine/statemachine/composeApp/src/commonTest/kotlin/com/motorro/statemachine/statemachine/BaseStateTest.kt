package com.motorro.statemachine.statemachine

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.statemachine.common.session.SessionManager
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.AppUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mock
import org.kodein.mock.UsesMocks
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
@UsesMocks(SessionManager::class, AppStateFactory::class, CommonStateMachine::class)
abstract class BaseStateTest : TestsWithMocks() {
    override fun setUpMocks() {
        mocker.injectMocks(this)
        every { stateMachine.setUiState(isAny()) } returns Unit
        every { stateMachine.setMachineState(isAny()) } returns Unit
    }

    @Mock
    lateinit var sessionManager: SessionManager
    @Mock
    lateinit var stateFactory: AppStateFactory
    @Mock
    lateinit var stateMachine: CommonStateMachine<AppGesture, AppUiState>

    protected lateinit var nextState: CommonMachineState<AppGesture, AppUiState>
    protected lateinit var dispatcher: TestDispatcher

    @BeforeTest
    fun init() {
        nextState = object : CommonMachineState<AppGesture, AppUiState>() { }
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun deinit() {
        Dispatchers.resetMain()
    }

    protected fun test(testBody: suspend TestScope.() -> Unit) = runTest(dispatcher) {
        testBody()
    }
}