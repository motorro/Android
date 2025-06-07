package com.motorro.architecture.car

fun main() {
    val car = Car(90)
    car.startUp()
    car.shutDown()
}

class Car(power: Int) {
    // Engine
    private val engine = Engine(power)

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