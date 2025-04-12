package com.motorro.network.server

import com.motorro.network.data.Profile
import com.motorro.network.data.User
import io.ktor.server.plugins.NotFoundException
import kotlin.time.Clock

interface Users {
    fun getUsers(): List<User>
    fun getProfile(userId: Int): Profile
    fun addUser(profile: Profile): User
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

    override fun addUser(profile: Profile): User {
        val newProfile = profile.copy(
            userId = profiles.maxOfOrNull { it.userId }?.plus(1) ?: 1,
            registered = Clock.System.now(),
        )
        profiles = profiles + newProfile

        return User(
            userId = newProfile.userId,
            name = newProfile.name,
            userpic = newProfile.userpic
        )
    }
}