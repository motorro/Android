package com.motorro.tasks.app.state

import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState
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
    protected val stateMachine: CommonStateMachine<AppGesture, AppUiState> = mockk(relaxed = true)
    protected val factory: AppStateFactory = mockk()
    protected val context: AppContext = mockk()
    protected val nextState: AppState = mockk()
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