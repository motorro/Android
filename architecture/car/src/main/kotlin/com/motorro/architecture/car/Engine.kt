package com.motorro.architecture.car

class Engine(private val power: Int) {
    /**
     * Start the car
     */
    fun startUp() {
        println("Dr-dr-dr: $power kW")
    }

    /**
     * Shutdown the car
     */
    fun shutDown() {
        println("Psh-sh..")
    }
}

