package com.motorro.architecture.registration.name

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.motorro.architecture.registration.TestStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class NameViewModelTest {
    private val minSymbols = 2
    private lateinit var dispatcher: TestDispatcher

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun deinit() {
        Dispatchers.resetMain()
    }

    @Test
    fun startsEmpty() = test {_, names, valids ->
        assertEquals("", names.last())
        assertEquals(false, valids.last())
    }

    @Test
    fun startsFromSaved() = test("user") {_, names, valids ->
        assertEquals("user", names.last())
        assertEquals(true, valids.last())
    }

    @Test
    fun updatesName() = test {model, names, _ ->
        model.setName("user")
        assertEquals("user", names.last())
    }

    @Test
    fun validIfNameIsLongEnough() = test {model, _, valids ->
        model.setName("u")
        assertEquals(false, valids.last())
        model.setName("us")
        assertEquals(true, valids.last())
    }


    private inline fun test(saved: String = "", crossinline check: suspend TestScope.(NameViewModel, List<String>, List<Boolean>) -> Unit) = runTest(dispatcher) {
        val viewModel = NameViewModel(TestStorage(saved), minSymbols)
        val names = mutableListOf<String>()
        backgroundScope.launch {
            viewModel.name.collect(names::add)
        }
        val isValids = mutableListOf<Boolean>()
        backgroundScope.launch {
            viewModel.nameIsValid.collect(isValids::add)
        }

        check(viewModel, names, isValids)
    }
}