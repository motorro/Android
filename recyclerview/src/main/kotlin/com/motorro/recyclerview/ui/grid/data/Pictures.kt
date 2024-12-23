package com.motorro.recyclerview.ui.grid.data

import androidx.annotation.DrawableRes
import com.github.javafaker.Faker
import com.motorro.recyclerview.R

/**
 * Picture
 */
data class Picture(val id: Int, @param:DrawableRes val picture: Int, val title: String)


private val faker = Faker()

@DrawableRes
private val pictures: List<Int> = listOf(
    R.drawable.ic_a,
    R.drawable.ic_help,
    R.drawable.ic_cup,
    R.drawable.ic_home,
    R.drawable.ic_flashlight,
    R.drawable.ic_health,
    R.drawable.ic_position
)

fun loadPictures(): List<Picture> = (1..21).map {
    Picture(
        id = it,
        picture = pictures.random(),
        title = faker.hipster().word()
    )
}