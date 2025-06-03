package com.motorro.architecture.registration.country

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.motorro.architecture.model.user.CountryCode
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
class CountryViewModelTest {
    private val countries = listOf(
        CountryCode("ARM") to "Armenia",
        CountryCode("AZE") to "Azerbaijan",
        CountryCode("RUS") to "Russia"
    )

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
    fun startsEmpty() = test {_, input, suggests, valids ->
        assertEquals("", input.last())
        assertEquals(countries.map { it.second }, suggests.last())
        assertEquals(false, valids.last())
    }

    @Test
    fun startsFromSaved() = test(CountryCode("RUS")) { _, input, suggests, valids ->
        assertEquals("Russia", input.last())
        assertEquals(listOf("Russia"), suggests.last())
        assertEquals(true, valids.last())
    }

    @Test
    fun updatesName() = test {model, input, _, _ ->
        model.setCountryName("ru")
        assertEquals("ru", input.last())
    }

    @Test
    fun validIfCountryFound() = test {model, _, _, valids ->
        model.setCountryName("russi")
        assertEquals(false, valids.last())
        model.setCountryName("russia")
        assertEquals(true, valids.last())
    }

    private fun test(saved: CountryCode = CountryCode.UNKNOWN, check: suspend TestScope.(CountryViewModel, List<String>, List<List<String>>, List<Boolean>) -> Unit) = runTest(dispatcher) {
        val viewModel = CountryViewModel(TestStorage(saved), countries)
        val input = mutableListOf<String>()
        backgroundScope.launch {
            viewModel.countryInput.collect(input::add)
        }
        val select = mutableListOf<List<String>>()
        backgroundScope.launch {
            viewModel.countrySelect.collect(select::add)
        }
        val isValids = mutableListOf<Boolean>()
        backgroundScope.launch {
            viewModel.countryIsValid.collect(isValids::add)
        }

        check(viewModel, input, select, isValids)
    }
}