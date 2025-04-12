package com.motorro.network.data

data class UserListItem(val user: User, val deleteEnabled: Boolean) {
    val userId: Int get() = user.userId
}