package com.motorro.architecture.registration.registering

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.motorro.architecture.core.error.UnknownException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.domain.profile.usecase.UpdateCurrentUserProfileUsecase
import com.motorro.architecture.model.user.CountryCode
import com.motorro.architecture.registration.TestStorage
import com.motorro.architecture.registration.data.RegistrationData
import com.motorro.architecture.registration.registering.data.RegisteringViewState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RegisteringViewModelTest {
    private lateinit var dispatcher: TestDispatcher
    private lateinit var register: UpdateCurrentUserProfileUsecase

    private val name = "name"
    private val country = CountryCode("RUS")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        dispatcher = UnconfinedTestDispatcher()
        register = mockk()
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun deinit() {
        Dispatchers.resetMain()
    }

    @Test
    fun registers() {
        every { register(any()) } returns flowOf(LceState.Loading(), LceState.Content(Unit))

        test { _, states ->
            assertEquals(
                RegisteringViewState.Complete,
                states.last()
            )

            verify {
                register(
                    withArg {
                        assertEquals(name, it.displayName)
                        assertEquals(country, it.countryCode)
                    }
                )
            }
        }
    }

    @Test
    fun retries() {
        every { register(any()) } returns flowOf(LceState.Loading(), LceState.Error(UnknownException(RuntimeException("Error"))))

        test { viewModel, states ->
            assertTrue { states.last() is RegisteringViewState.Error }

            viewModel.retry()

            verify(exactly = 2) {
                register(
                    withArg {
                        assertEquals(name, it.displayName)
                        assertEquals(country, it.countryCode)
                    }
                )
            }
        }
    }

    private inline fun test(crossinline check: suspend TestScope.(RegisteringViewModel, List<RegisteringViewState>) -> Unit) = runTest(dispatcher) {
        val regData: RegistrationData = mockk {
            every { this@mockk.name } returns TestStorage(this@RegisteringViewModelTest.name)
            every { this@mockk.country } returns TestStorage(this@RegisteringViewModelTest.country)
        }

        val viewModel = RegisteringViewModel(regData, register)
        val states = mutableListOf<RegisteringViewState>()
        backgroundScope.launch {
            viewModel.viewState.collect(states::add)
        }

        check(viewModel, states)
    }
}