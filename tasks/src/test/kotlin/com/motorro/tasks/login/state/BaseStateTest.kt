package com.motorro.tasks.login.state

import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.tasks.login.data.LoginGesture
import com.motorro.tasks.login.data.LoginUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After

@OptIn(ExperimentalCoroutinesApi::class)
internal open class BaseStateTest {
    protected val stateMachine: CommonStateMachine<LoginGesture, LoginUiState> = mockk(relaxed = true)
    protected val factory: LoginStateFactory = mockk()
    protected val context: LoginContext = mockk()
    protected val nextState: LoginState = mockk()
    protected val dispatcher = UnconfinedTestDispatcher()

    init {
        every { context.factory } returns factory
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }
}