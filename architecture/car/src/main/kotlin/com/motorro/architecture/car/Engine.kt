package com.motorro.architecture.car

interface Engine {
    /**
     * Start the car
     */
    fun startUp()

    /**
     * Shutdown the car
     */
    fun shutDown()
}

class PetrolEngine(private val power: Int) : Engine {
    override fun startUp() {
        println("Dr-dr-dr: $power kW")
    }

    override fun shutDown() {
        println("Psh-sh..")
    }
}

class ElectricEngine(private val power: Int): Engine {
    override fun startUp() {
        println("Hm-m-m-m: $power kW")
    }

    override fun shutDown() {
        println("...")
    }
}