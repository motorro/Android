package com.motorro.architecture.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.domain.profile.error.NotRegisteredException
import com.motorro.architecture.domain.session.error.UnauthorizedException
import com.motorro.architecture.main.data.MainScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var dispatcher: TestDispatcher
    private lateinit var profileFlow: MutableSharedFlow<LceState<Any, CoreException>>
    private lateinit var model: MainViewModel

    @Before
    fun init() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        profileFlow = MutableSharedFlow()
        model = MainViewModel(profileFlow)
    }

    @After
    fun deinit() {
        Dispatchers.resetMain()
    }

    private inline fun test(crossinline block: suspend TestScope.(List<MainScreenState>) -> Unit) = runTest(dispatcher) {
        val state = mutableListOf<MainScreenState>()
        backgroundScope.launch {
            model.state.collect(state::add)
        }

        block(state)
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun showsContentOnLoading() = test {
        assertEquals(MainScreenState.Content, it.last())
    }

    @Test
    fun showsContentOnContent() = test {
        profileFlow.emit(LceState.Content(Unit))

        assertEquals(MainScreenState.Content, it.last())
    }

    @Test
    fun showsAuthOnNonAuthorized() = test {
        profileFlow.emit(LceState.Error(UnauthorizedException()))

        assertEquals(MainScreenState.Authenticating, it.last())
    }

    @Test
    fun showsRegisterOnNotRegistered() = test {
        profileFlow.emit(LceState.Error(NotRegisteredException()))

        assertEquals(MainScreenState.Registering, it.last())
    }
}