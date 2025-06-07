package com.motorro.architecture.car

fun main() {
    val pCar = Car(PetrolEngine(100))
    pCar.startUp()
    pCar.shutDown()

    val eCar = Car(ElectricEngine(100))
    eCar.startUp()
    eCar.shutDown()
}

class Car(private val engine: Engine) {
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