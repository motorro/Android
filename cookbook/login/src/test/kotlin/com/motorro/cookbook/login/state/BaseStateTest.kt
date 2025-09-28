package com.motorro.cookbook.login.state

import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.cookbook.appcore.navigation.auth.AuthFlowHost
import com.motorro.cookbook.login.data.LoginGesture
import com.motorro.cookbook.login.data.LoginViewState
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
    protected lateinit var stateMachine: CommonStateMachine<LoginGesture, LoginViewState>
    protected lateinit var factory: LoginStateFactory

    protected lateinit var context: LoginContext

    protected lateinit var nextState: LoginState

    protected lateinit var flowHost: AuthFlowHost

    protected open fun createDispatcher(): TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun init() {
        dispatcher = createDispatcher()
        Dispatchers.setMain(dispatcher)

        stateMachine = mockk(relaxed = true)
        factory = mockk()
        nextState = mockk()
        flowHost = mockk()

        context = object : LoginContext {
            override val factory: LoginStateFactory
                get() = this@BaseStateTest.factory
            override val flowHost: AuthFlowHost
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