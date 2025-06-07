package com.motorro.architecture.car

fun main() {
    val car = Car()
    car.startUp()
    car.shutDown()
}

class Car {
    // Engine
    private val engine = Engine()

    /**
     * Start the car
     */
    fun startUp() {
        engine.startUp()
    }

    /**
     * Shutdown the car
     */
    fun shutDown() {
        engine.shutDown()
    }
}