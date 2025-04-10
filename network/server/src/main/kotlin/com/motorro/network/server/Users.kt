package com.motorro.network.server

import com.motorro.network.data.Profile
import com.motorro.network.data.User
import io.ktor.server.plugins.NotFoundException

interface Users {
    fun getUsers(): List<User>
    fun getProfile(userId: Int): Profile
}

class UsersImpl(private var profiles: List<Profile>) : Users{
    override fun getUsers(): List<User> = profiles.map { profile ->
        User(
            userId = profile.userId,
            name = profile.name,
            userpic = profile.userpic
        )
    }

    override fun getProfile(userId: Int): Profile = profiles.find { it.userId == userId } ?: throw NotFoundException("User with id $userId not found")
}