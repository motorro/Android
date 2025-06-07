package com.motorro.architecture.car

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class CarTest {
    private lateinit var engine: Engine
    private lateinit var car: Car

    @Before
    fun init() {
        engine = mockk()
        car = Car(engine)
    }

    @Test
    fun whenCarStartsTheEngineStarts() {
        every { engine.startUp() } just Runs

        // when
        car.startUp()

        // then
        verify { engine.startUp() }
    }

    @Test
    fun whenCarShutsDownTheEngineShutsDown() {
        every { engine.shutDown() } just Runs

        // when
        car.shutDown()

        // then
        verify { engine.shutDown() }
    }
}