package ru.merionet.tasks.app.state

import com.motorro.commonstatemachine.CommonStateMachine
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import ru.merionet.tasks.app.data.AppGesture
import ru.merionet.tasks.app.data.AppUiState

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