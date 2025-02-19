package com.motorro.fragments.data

import com.motorro.fragments.R

private val HAMBURGER = Item(
    id = ItemId(id = 1),
    name = "Hambuger",
    price = 3
)

private val CHEESEBURGER = Item(
    id = ItemId(id = 2),
    name = "Cheeseburger",
    price = 5
)

private val ROYALE = Item(
    id = ItemId(id = 3),
    name = "Royale",
    price = 7
)

private val FRIES = Item(
    id = ItemId(id = 4),
    name = "Fries",
    price = 2
)

private val SALAD = Item(
    id = ItemId(id = 5),
    name = "Salad",
    price = 3
)

private val COLA = Item(
    id = ItemId(id = 6),
    name = "Cola",
    price = 1
)

private val COFFEE = Item(
    id = ItemId(id = 7),
    name = "Coffee",
    price = 2
)

val menu = listOf(
    Category(
        id = CategoryId(id = 1),
        name = "Burger",
        image = R.drawable.ic_burger,
        items = listOf(
            HAMBURGER,
            CHEESEBURGER,
            ROYALE
        )
    ),
    Category(
        id = CategoryId(id = 2),
        name = "Side dishes",
        image = R.drawable.ic_french_fries,
        items = listOf(
            FRIES,
            SALAD
        )
    ),
    Category(
        id = CategoryId(id = 3),
        name = "Drinks",
        image = R.drawable.ic_drink,
        items = listOf(
            COFFEE,
            COLA
        )
    )
)

