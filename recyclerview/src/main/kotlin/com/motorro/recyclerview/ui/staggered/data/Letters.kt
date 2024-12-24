package com.motorro.recyclerview.ui.staggered.data

import kotlin.random.Random

data class Letter(val symbol: Char, val count: Int)

fun loadLetters(): List<Letter> = (0..25).map {
    Letter(
        'A' + it,
        Random.nextInt(0, 25)
    )
}